package com.escpos.react;

import android.util.Log;

import androidx.annotation.NonNull;

import com.dantsu.escposprinter.connection.tcp.TcpConnection;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PosPrinterModule extends ReactContextBaseJavaModule {
    public PosPrinterModule(ReactApplicationContext context) {
        super(context);
    }

    @NonNull
    @Override
    public String getName() {
        return "PosPrinter";
    }

    @ReactMethod
    public void scanNetworkPrinters(String baseIp, int start, int end, Promise promise) {
        new Thread(() -> {
            List<String> found = new ArrayList<>();
            for (int i = start; i <= end; i++) {
                String ip = baseIp + i;
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(ip, 9100), 150);
                    found.add(ip);
                } catch (IOException e) {
                    // ignore
                }
            }
            WritableArray array = Arguments.createArray();
            for (String ip : found) {
                array.pushString(ip);
            }
            promise.resolve(array);
        }).start();
    }

    @ReactMethod
    public void print(String ip, int port, String text, Promise promise) {
        new Thread(() -> {
            TcpConnection connection = new TcpConnection(ip, port);
            try {
                connection.connect();
                connection.write(text.getBytes());
                connection.send(0x0A);
                connection.disconnect();
                promise.resolve(null);
            } catch (EscPosConnectionException | IOException e) {
                Log.e("PosPrinter", "Print error", e);
                promise.reject("PRINT_ERROR", e);
            }
        }).start();
    }
}
