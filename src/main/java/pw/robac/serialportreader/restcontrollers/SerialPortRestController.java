package pw.robac.serialportreader.restcontrollers;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
//@RequestMapping("/serial")
public class SerialPortRestController {

    private SerialPort serialPort;

    @GetMapping
    public List<String> getSerialPotsNames(){
        return Arrays.stream(SerialPort.getCommPorts())
                .map(SerialPort::getSystemPortName)
                .collect(Collectors.toList());
    }

    @GetMapping("/websys-mvc-rest-cors-1.0.0/waga/")
    public String zwrocWartosc(){
        String value = "0 ";
        initSerialPort();
        try {
            while (serialPort.bytesAvailable() == 0)
                Thread.sleep(20);

            byte[] readBuffer = new byte[serialPort.bytesAvailable()];
            int numRead = serialPort.readBytes(readBuffer, readBuffer.length);
//            serialPort.re
            value = String.valueOf(numRead);
            System.out.println("Read " + numRead + " bytes.");
        } catch (Exception e) { e.printStackTrace(); }
        return value;
    }

    @GetMapping("value")
    public String getValue(){
        String value = "";
        initSerialPort();
        try {
                while (serialPort.bytesAvailable() == 0)
                    Thread.sleep(20);

                byte[] readBuffer = new byte[serialPort.bytesAvailable()];
                int numRead = serialPort.readBytes(readBuffer, readBuffer.length);
                value = String.valueOf(numRead);
                System.out.println("Read " + numRead + " bytes.");
                System.out.println("VALUE TO: "+new String(readBuffer));
        } catch (Exception e) { e.printStackTrace(); }
        return value;
    }

    private void initSerialPort(){
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            System.out.println("PORT ZAMKNIETY");
        }
        serialPort = SerialPort.getCommPort("ttyS0");
        serialPort.setParity(SerialPort.NO_PARITY);
        serialPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
        serialPort.setNumDataBits(8);
//        serialPort.addDataListener(this);
        serialPort.setBaudRate(9600);
        serialPort.openPort();
        System.out.println("PORT OWARTY");
    }
}
