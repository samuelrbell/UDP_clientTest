import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException
import javax.sound.sampled.*

object Client {
    @Throws(LineUnavailableException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val format = AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100F, 16, 2, 4, 44100F, true)
        var microphone: TargetDataLine
        val speakers: SourceDataLine
        microphone = AudioSystem.getTargetDataLine(format)
        val info = DataLine.Info(TargetDataLine::class.java, format)
        microphone = AudioSystem.getLine(info) as TargetDataLine
        microphone.open(format)
        val out = ByteArrayOutputStream()
        var numBytesRead: Int
        val CHUNK_SIZE = 1024
        val data = ByteArray(microphone.bufferSize / 5)
        microphone.start()
        val bytesRead = 0
        val dataLineInfo = DataLine.Info(SourceDataLine::class.java, format)
        speakers = AudioSystem.getLine(dataLineInfo) as SourceDataLine
        speakers.open(format)
        speakers.start()
        val hostname = "localhost"
        val port = 5555
        try {
            val address = InetAddress.getByName(hostname)
            val socket = DatagramSocket()
            val serverSocket = DatagramSocket(5555)
            val receiveData = ByteArray(1024)
            val sendData = ByteArray(1024)
            while (true) {
                val buffer = ByteArray(1024)
                val response = DatagramPacket(buffer, buffer.size)
                serverSocket.receive(response)
                out.write(response.data, 0, response.data.size)
                speakers.write(response.data, 0, response.data.size)
                val quote = String(buffer, 0, response.length)
                println(quote)
                println()

                //Thread.sleep(10000);
            }
        } catch (ex: SocketTimeoutException) {
            println("Timeout error: " + ex.message)
            ex.printStackTrace()
        } catch (ex: IOException) {
            println("Client error: " + ex.message)
            ex.printStackTrace()
        } /* catch (InterruptedException ex) {
        ex.printStackTrace();
    }*/
    }
}