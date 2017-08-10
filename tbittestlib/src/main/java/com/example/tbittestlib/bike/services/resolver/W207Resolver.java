package com.example.tbittestlib.bike.services.resolver;

import com.example.tbittestlib.bike.model.BikeState;
import com.example.tbittestlib.bike.model.ControllerState;
import com.example.tbittestlib.bluetooth.util.ByteUtil;
import com.example.tbittestlib.entity.BControllerState;
import com.example.tbittestlib.entity.W207State;

import java.util.Arrays;

import static com.example.tbittestlib.bike.util.StateUpdateHelper.bitResolver;
import static com.example.tbittestlib.bike.util.StateUpdateHelper.byteArrayToInt;
import static com.example.tbittestlib.bike.util.StateUpdateHelper.byteToBitArray;
import static com.example.tbittestlib.bike.util.StateUpdateHelper.updateBaseStation;
import static com.example.tbittestlib.bike.util.StateUpdateHelper.updateLocation;
import static com.example.tbittestlib.bike.util.StateUpdateHelper.updateSignal;
import static com.example.tbittestlib.bike.util.StateUpdateHelper.updateVoltage;

/**
 * Created by Salmon on 2017/4/27 0027.
 */

public class W207Resolver implements Resolver<W207State> {

    @Override
    public void resolveAll(BikeState bikeStates, Byte[] data) {
        if (data == null || data.length == 0)
            return;

        bikeStates.setRawData(data);

        if (data.length >= 11) {
            Byte[] locationData = Arrays.copyOfRange(data, 0, 8);
            updateLocation(bikeStates, locationData);
            Byte[] heading = Arrays.copyOfRange(data, 8, 11);
            bikeStates.setGpsState(bitResolver(heading[0], 16));
        }
        if (data.length >= 14) {
            Byte[] signalData = Arrays.copyOfRange(data, 11, 14);
            updateSignal(bikeStates, signalData);
        }
        if (data.length >= 16) {
            Byte[] batteryData = Arrays.copyOfRange(data, 14, 16);
            updateVoltage(bikeStates, batteryData);
        }
        if (data.length >= 24) {
            Byte[] baseStationData = Arrays.copyOfRange(data, 16, 24);
            updateBaseStation(bikeStates, baseStationData);
        }
        if (data.length >= 44) {
            Byte[] controllerInfoData = Arrays.copyOfRange(data, 24, 44);
            resolveControllerState(bikeStates, controllerInfoData);
        }
    }

    @Override
    public void resolveControllerState(BikeState bikeState, Byte[] data) {
        if (data == null || data.length < 19)
            return;
        ControllerState controllerState = bikeState.getControllerState();

        controllerState.setRawData(data);
    }

    @Override
    public void resolveLocations(BikeState bikeState, Byte[] data) {
        if (data.length < 11)
            return;
        double[] result = ByteUtil.getPoint(data);
        bikeState.setLocation(result);

        Byte[] heading = Arrays.copyOfRange(data, 8, 10);
        bikeState.setGpsState(bitResolver(heading[0], 16));
    }

    @Override
    public W207State resolveCustomState(BikeState bikeState) {
        W207State state = new W207State();

        double[] longitudes = resolveLocations(bikeState.getLocation()[0]);
        double[] latitude = resolveLocations(bikeState.getLocation()[1]);

        state.setLongitudeDegree(longitudes[0]);
        state.setLongitudeMinute(longitudes[1]);
        state.setLatitudeDegree(latitude[0]);
        state.setLatitudeMinute(latitude[1]);

        state.setSatellite(bikeState.getSignal()[1]);

        state.setGpsState(bikeState.getGpsState());

        BControllerState controllerState = resolveBControllerState(bikeState.getControllerState().getRawData());

        state.setTotalMileage(controllerState.getTotalMillage());
        state.setBattery(controllerState.getVoltage());

        int[] sysState = bikeState.getSystemState();

        state.setCharging(sysState[2] == 1);

        int[] status2 = controllerState.getStatus2();
        int[] errCode = new int[]{0,0,0,0,0,0,0,0,0};
        System.arraycopy(status2, 0, errCode, 0, status2.length);
        state.setErrorCode(errCode);

        state.setChargeCount(controllerState.getChargeCount());

        state.setSustained(sysState[7] == 1);

        String res = String.valueOf(sysState[1]) + String.valueOf(sysState[0]);

        int flag = Integer.valueOf(res, 2);

        int ctrlValue = 1;
        switch (flag) {
            case 0:
                ctrlValue = 2;
                break;
            case 1:
                ctrlValue = 1;
                break;
            case 2:
                ctrlValue = 3;
                break;
        }
        state.setCtrlState(ctrlValue);

        return state;
    }

    // 0:degree 1:minute
    public static double[] resolveLocations(double d) {
        double[] locations = new double[]{0, 0};

        try {
            double degree = (double) ((int) (d));

            locations[0] = degree;

            double min = (d % 1) * 60;

            String degreeStr = String.valueOf(min);

            min = Double.parseDouble(degreeStr.substring(0, degreeStr.indexOf(".") + 5));

            locations[1] = min;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return locations;
    }

    private BControllerState resolveBControllerState(Byte[] data) {
        BControllerState controllerState = new BControllerState();
        if (data == null || data.length < 19)
            return controllerState;

        byte[] originData = ByteUtil.byteArrayToUnBoxed(data);

        int[] status2 = controllerState.getStatus2();
        Byte b2 = data[1];
        byteToBitArray(b2, status2);

        int[] status3 = controllerState.getStatus3();
        Byte b3 = data[2];
        byteToBitArray(b3, status3);

        int[] status4 = controllerState.getStatus4();
        Byte b4 = data[3];
        byteToBitArray(b4, status4);

        controllerState.setMovingEi(byteArrayToInt(Arrays.copyOfRange(originData, 4, 5)));

        byte[] chargeData = Arrays.copyOfRange(originData, 5, 7);
        chargeData = new byte[]{chargeData[1], chargeData[0]};
        controllerState.setChargeCount(byteArrayToInt(chargeData));

        controllerState.setVoltage(byteArrayToInt(Arrays.copyOfRange(originData, 7, 8)));

        controllerState.setHumidity(byteArrayToInt(Arrays.copyOfRange(originData, 8, 9)));

        controllerState.setTotalMillage(byteArrayToInt(Arrays.copyOfRange(originData, 9, 13)));

        controllerState.setSingleMillage(byteArrayToInt(Arrays.copyOfRange(originData, 13, 17)));

        controllerState.setExtVoltage(byteArrayToInt(Arrays.copyOfRange(originData, 17, 19)));

        return controllerState;
    }
}
