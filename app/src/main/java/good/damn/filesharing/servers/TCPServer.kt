package good.damn.filesharing.servers

import android.util.Log
import good.damn.filesharing.Application
import good.damn.filesharing.listeners.network.server.ServerListener
import good.damn.filesharing.services.network.request.ResponseService
import java.io.ByteArrayOutputStream
import java.net.ServerSocket
import java.net.SocketException
import java.net.SocketTimeoutException

open class TCPServer(
    port: Int
): BaseServer<ServerListener>(
    port
), Runnable {

    private val TAG = "TCPServer"

    private var mServer: ServerSocket? = null

    private val mResponseService = ResponseService()

    final override var delegate: ServerListener?
        get() = super.delegate
        set(value) {
            mResponseService.delegate = value
            super.delegate = value
        }

    final override fun run() {
        mServer = onCreateSocket()
        mServer?.reuseAddress = true
        
        delegate?.onCreateServer(
            mServer!!
        )

        while (listen(Application.BUFFER_MB)) {
            // Listen...
        }
    }

    override fun serverType(): String {
        return "No SSL"
    }

    final override fun start() {
        Thread(this)
            .start()
    }

    final override fun stop() {
        mServer?.close()
        mServer = null
        delegate?.onDropServer()
    }

    internal open fun onCreateSocket(): ServerSocket {
        return ServerSocket(
            port
        )
    }

    private fun listen(
        buffer: ByteArray
    ): Boolean {

        delegate?.onStartListen()
        try {
            val clientSocket = mServer!!.accept()
            clientSocket.soTimeout = 13000

            val out = clientSocket.getOutputStream()
            val inp = clientSocket.getInputStream()

            val outArr = ByteArrayOutputStream()

            var n: Int

            Thread.sleep(500)
            while (true) {
                Log.d(TAG, "listen: READ ${inp.available()} ${outArr.size()}")
                if (inp.available() == 0) {
                    break
                }

                n = inp.read(buffer)
                Log.d(TAG, "listen: READ $n ${outArr.size()}")
                delegate?.onListenChunkData(
                    buffer,
                    n,
                    inp.available()
                )

                if (n == -1) {
                    break
                }

                outArr.write(
                    buffer,
                    0,
                    n
                )
            }

            val data = outArr.toByteArray()
            outArr.close()

            Log.d(TAG, "listen: DATA: ${data.size}")

            val response = mResponseService
                .manage(data)

            if (mServer == null || response.isEmpty()) {
                out.close()
                return false
            }

            out.write(
                response
            )

            delegate?.onDropClient(
                clientSocket
            )

            out.close()
        } catch (e: SocketException) {
            Log.d(TAG, "listen: EXCEPTION:  ${e.message}")
            return false
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "listen: TIMED_OUT: ${e.message}")
        }

        return true
    }
}