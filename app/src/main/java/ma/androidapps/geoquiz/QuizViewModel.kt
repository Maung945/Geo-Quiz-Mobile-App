package ma.androidapps.geoquiz

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"

class QuizViewModel (private val savedStateHandle: SavedStateHandle) : ViewModel() {
    /*
    init {
        Log.d(TAG, "ViewModel instance created")
    }
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }
    */
    val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),

        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true),
        /*
        Question(R.string.question_russia, true),
        Question(R.string.question_river_thames, false),
        Question(R.string.question_river_severn, true),
        Question(R.string.question_volcano, true),
        Question(R.string.question_dolphins, true),
        Question(R.string.question_rivers, false),
        Question(R.string.question_mtsnowdon, true),
         */
        Question(R.string.question_river_bank, false),

        Question(R.string.question_tuvalu, false),
        Question(R.string.question_norway, false),

        //Question(R.string.question_milan, true),
        //Question(R.string.question_kabul, true),
        //Question(R.string.question_bergamo, true),


        Question(R.string.question_china, true)
    )

    var isCheater: Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

    var currentIndex : Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    var currentScore : Int = 0

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex = if(currentIndex !=0 ) (currentIndex - 1) % questionBank.size else questionBank.size - 1
    }

    fun moveToBeginning() {
        currentIndex = 0
    }
}