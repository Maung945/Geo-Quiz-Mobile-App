
package ma.androidapps.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import ma.androidapps.geoquiz.databinding.ActivityMainBinding
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle the result
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        binding.trueButton.setOnClickListener { view: View->

            binding.trueButton.isEnabled=false
            binding.falseButton.isEnabled=false
            //questionBank[currentIndex].answered=true
            //quizViewModel.currentQuestionAnswer = true
            checkAnswer(true)
        }
        //falseButton.setOnClickListener { view: View ->
        binding.falseButton.setOnClickListener { view: View->
            binding.trueButton.isEnabled=false
            binding.falseButton.isEnabled=false

            //quizViewModel.currentQuestionAnswer = true
            checkAnswer(false)
        }

        binding.nextButton.setOnClickListener {
            /*
            currentIndex = (currentIndex + 1) %  questionBank.size
            isAnswered(currentIndex)
            updateQuestion()
             */
            //currentIndex = (currentIndex + 1) %  questionBank.size
            quizViewModel.moveToNext()
            //isAnswered(currentIndex)
            updateQuestion()
            if(quizViewModel.currentIndex == 0) {
                quizViewModel.currentScore = 0
            }
        }

        binding.prevButton.setOnClickListener {
            /*
            currentIndex = (currentIndex - 1) % questionBank.size
            if(currentIndex == -1) {
                currentIndex = questionBank.lastIndex
            }
            isAnswered(currentIndex)
            updateQuestion()
            */
            //currentIndex = if(currentIndex !=0 ) (currentIndex - 1) % questionBank.size else questionBank.size - 1
            quizViewModel.moveToPrev()
            //isAnswered(currentIndex)
            updateQuestion()
        }

        binding.cheatButton.setOnClickListener {
            // Start CheatActivity
            //val intent = Intent(this, CheatActivity::class.java)
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            //startActivity(intent)
            cheatLauncher.launch(intent)
        }

        binding.restartButton.setOnClickListener {
            quizViewModel.moveToBeginning()
            //isAnswered(currentIndex)
            updateQuestion()
            if(quizViewModel.currentIndex == 0) {
                quizViewModel.currentScore = 0
            }
        }

        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
        switchAnsButtonTo(true)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        /*
        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
         */
        val messageResId = when {
            //quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        // Add
        if(userAnswer == correctAnswer) {
            quizViewModel.currentScore++
            Log.d(TAG, quizViewModel.currentScore.toString())
        }

        val toast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 20)
        toast.show()

        //Add
        if(quizViewModel.currentIndex == quizViewModel.questionBank.size - 1) {
            showScore()
        }
    }

    private fun isAnswered(index:Int){
        if (quizViewModel.questionBank[index].answered == true){
            binding.trueButton.isEnabled = false
            binding.falseButton.isEnabled = false
        }else{
            binding.trueButton.isEnabled = true
            binding.falseButton.isEnabled = true
        }
    }

    private fun showScore() {
        val percent = ((quizViewModel.currentScore.toDouble() / quizViewModel.questionBank.size) * 100).toInt()
        val stringScore = "Answered $percent% correctly!!"
        Toast.makeText(this, stringScore, Toast.LENGTH_SHORT).show()
    }


    private fun switchAnsButtonTo(b: Boolean) {
        if (!b) {
            binding.trueButton.isEnabled = false
            binding.falseButton.isEnabled = false
        } else {
            binding.trueButton.isEnabled = true
            binding.falseButton.isEnabled = true
        }
    }
}
