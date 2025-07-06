import { NativeModules } from 'react-native';

const { PosPrinter } = NativeModules;

export default {
  scanNetworkPrinters(baseIp, start = 1, end = 254) {
    return PosPrinter.scanNetworkPrinters(baseIp, start, end);
  },

  print(ip, port, text) {
    return PosPrinter.print(ip, port, text);
  },
};
