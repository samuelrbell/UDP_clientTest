import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.Inet4Address
import java.util.*
import kotlin.jvm.Throws

object UDPClient {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val port = 8010
        val datagramSocket = DatagramSocket(port)
        val findCoords = """OP-4 COORDs: (\d+)\.(\d+)\.(\d+)""".toRegex()
        val findIP = """(\d+)\.(\d+)\.(\d+)\.(\d+)""".toRegex()
        println("Client is running...")

        // Packet Transfer

        // Interfacing with the server...
        val clientAddress = Inet4Address.getLocalHost().toString()
        val foundAddress = findIP.find(clientAddress)?.value
        val dataString = "OP REQUEST: IP: $foundAddress PORT: $port"
        println(dataString)
        println(dataString)
        var datagramPacket = DatagramPacket(
            dataString.toByteArray(),
            dataString.toByteArray().size,
            Inet4Address.getByName("LAPTOP-01E51TRD"),
            8000
        )
        datagramSocket.send(datagramPacket)

        //Receiving...

        try {
            while(true){

                val time = Date().toString()
                val messageTo1 = "OP-4 COORDs: 94.34.12 -- "
                val mes1 = (messageTo1 +  time).toByteArray()

                datagramPacket = DatagramPacket(
                    mes1,
                    mes1.size,
                    Inet4Address.getByName("LAPTOP-01E51TRD"),
                    8000
                )
                datagramSocket.send(datagramPacket)

                // Receiving messages

                datagramPacket = DatagramPacket(ByteArray(1024),1024)
                datagramSocket.receive(datagramPacket)
                val data = datagramPacket.data
                val dataString = String(data,0,data.size)

                if(!findCoords.containsMatchIn(dataString)) {
                    println("Received: $dataString")
                }
        }
    } catch (i: IOException) {
        println("ERROR: IOException")
    }

        datagramSocket.close()
    }
}