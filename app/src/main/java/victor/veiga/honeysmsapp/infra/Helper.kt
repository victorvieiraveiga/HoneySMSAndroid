package victor.veiga.honeysmsapp.infra

import java.text.SimpleDateFormat
import java.util.*

class Helper {

    fun DateFormat(date: String): String {
        val d = date//2020-01-08T12:42:45.6079611    "2020-05-08T11:01:48.3300000Z"
        val originalFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS")
        val targetFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date: Date = originalFormat.parse(d)
        val formattedDate: String = targetFormat.format(date)
        return formattedDate

    }

}