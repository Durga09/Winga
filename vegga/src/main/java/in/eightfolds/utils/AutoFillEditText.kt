package `in`.eightfolds.utils

import `in`.eightfolds.WingaApplication
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import com.google.android.material.textfield.TextInputEditText
import android.util.AttributeSet
import android.view.autofill.AutofillValue

class AutoFillEditText(context: Context?, attrs: AttributeSet?) : TextInputEditText(context?:WingaApplication.applicationContext, attrs) {

    @TargetApi(Build.VERSION_CODES.O)
    override fun autofill(value: AutofillValue) {
        val autofilledText = value.textValue
        val processedText = sanitize(autofilledText.toString())

        super.autofill(AutofillValue.forText(processedText))
    }

    private fun  sanitize(text : String) : CharSequence{
        var formattedText = text
        if(text.startsWith("+1")){
            formattedText = text.replaceRange(0,2,"")
        }
        if(text.startsWith("+91")){
            formattedText = text.replaceRange(0,2,"")
        }
        formattedText = formattedText.trim()
        if(formattedText.contains("-")){
            formattedText = formattedText.replace("-","")
        }
        if(formattedText.contains(" ")){
            formattedText = formattedText.replace(" ","")
        }

        return formattedText
    }
}