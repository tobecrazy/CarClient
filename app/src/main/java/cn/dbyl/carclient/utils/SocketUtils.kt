package cn.dbyl.carclient.utils

import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.net.UnknownHostException


/**
 * Create by young on 11/24/2019
 **/
object SocketUtils {
    fun connectServerWithTCPSocket(ip: String, port: Int = 1989) {
        val socket: Socket
        try { // 创建一个Socket对象，并指定服务端的IP及端口号
            socket = Socket(ip, port)
            // 创建一个InputStream用户读取要发送的文件。
            val inputStream: InputStream = FileInputStream("e://a.txt")
            // 获取Socket的OutputStream对象用于发送数据。
            val outputStream: OutputStream = socket.getOutputStream()
            // 创建一个byte类型的buffer字节数组，用于存放读取的本地文件
            val buffer = ByteArray(4 * 1024)
            var temp = 0
            // 循环读取文件
            while (inputStream.read(buffer).also({ temp = it }) != -1) { // 把数据写入到OuputStream对象中
                outputStream.write(buffer, 0, temp)
            }
            // 发送读取的数据到服务端
            outputStream.flush()
            //          String socketData = "[2143213;21343fjks;213]";
//          BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
//                  socket.getOutputStream()));
//          writer.write(socketData.replace("\n", " ") + "\n");
//          writer.flush();
            /** */
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}