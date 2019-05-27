package me.funwarioisii.practflite

import android.content.res.AssetManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel.MapMode.READ_ONLY


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val modelFilename = "models/helloworld.tflite"
        val tfLiteOptions = Interpreter.Options()
        tfLiteOptions.setNumThreads(1)

        val tfLiteModel = loadModelFile(this.assets, modelFilename)
        val tfLite = Interpreter(tfLiteModel, tfLiteOptions)

        val inputByteBuffer = ByteBuffer.allocateDirect(4)
        val resultArray: Array<String> = Array(1) {""}
        tfLite.run(inputByteBuffer, resultArray)
        Log.d("result:", resultArray[0])
    }

    @Throws(IOException::class)
    private fun loadModelFile(assets: AssetManager, modelFilename: String): MappedByteBuffer {
        val fileDescriptor = assets.openFd(modelFilename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(READ_ONLY, startOffset, declaredLength)
    }
}