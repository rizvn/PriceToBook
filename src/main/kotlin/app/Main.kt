import org.apache.commons.csv.CSVFormat
import org.junit.Test
import spark.Route
import java.io.InputStreamReader
import java.net.URL
import spark.Spark.*

fun priceToBook(symbol: String): String{
  val url = "http://financials.morningstar.com/ajax/exportKR2CSV.html?t=${symbol}"
  val response = InputStreamReader(URL(url).openStream())
  val csv = CSVFormat.EXCEL.parse(response)
  csv.forEach {
    val rowName = it.get(0)
    if (rowName.contains("Book Value Per Share")){
      return it.get(it.size()-1)
    }
  }
  return "-1";
}

fun main(args:Array<String>){
  get("/p2b/:symbol", Route { request, response ->
    priceToBook(request.params(":symbol"))
  })
}

class TestClass{
  @Test
  fun testPriceToBook(){
    println(priceToBook("barc"))
  }
}