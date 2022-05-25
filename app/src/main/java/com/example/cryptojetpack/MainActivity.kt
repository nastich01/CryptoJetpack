package com.example.cryptojetpack
import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.cryptojetpack.ui.theme.CryptoJetpackTheme
import okhttp3.*
import org.json.JSONArray
import java.io.IOException


class MainActivity : ComponentActivity() {
    private val client = OkHttpClient()
    private var  coin: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val valName = remember{mutableStateOf("")}
            val imgurl = remember{mutableStateOf("")}
            Column() {
                Column(modifier = Modifier
                    .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

                    val text =  remember { mutableStateOf("") }
                    TextField(value = text.value, onValueChange = {text.value = it}, label = {Text(text="Введите криптовалюту")})
                    coin=text.value
                    Button(onClick = { valName.value=kk(coin,client)+"USD"
                                    imgurl.value = "https://www.cryptocompare.com/"+imgkk(coin,client)
                    }){
                        Text("Получить данные", fontSize = 25.sp)
                    }
                    TextField(
                        value = valName.value,
                        onValueChange = {newText -> valName.value = newText},
                        textStyle = TextStyle(fontSize=25.sp),
                        enabled = false
                    )
                    Image(
                        painter = rememberAsyncImagePainter(imgurl.value),
                        contentDescription = null,
                        modifier = Modifier.size(128.dp)
                    )
                }
            }

        }
    }

}

fun kk (coin:String,client:OkHttpClient):String{
    var ok: String = "5"
    val request = Request.Builder()
        .url("https://min-api.cryptocompare.com/data/pricemulti?fsyms="+coin+"&tsyms=USD&api_key=4a71c8c7dca54b9a6af080ba0a9a8ee2c1fe45299e8dfb57e82145f710fa04f2")
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {}
        override fun onResponse(call: Call, response: Response) {
            ok = response.body?.string().toString()
            println(ok)

            ok = ok.substringAfter("USD").substringBefore("}}").substring(2)
            println(ok.toFloatOrNull())
        }
    })
    Thread.sleep(1000)
    return ok
}

fun imgkk (coin:String,client:OkHttpClient):String{
    var ok: String = "5"
    val request = Request.Builder()
        .url("https://min-api.cryptocompare.com/data/pricemultifull?fsyms="+coin+"&tsyms=USD&api_key=4a71c8c7dca54b9a6af080ba0a9a8ee2c1fe45299e8dfb57e82145f710fa04f2")
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {}
        override fun onResponse(call: Call, response: Response) {
            ok = response.body?.string().toString()
            println(ok)

            ok = ok.substringAfter("IMAGEURL").substringBefore("}").substring(4)
            ok = ok.substring(0,ok.length-1)
            println(ok)
        }
    })

    Thread.sleep(1000)
    return ok
}