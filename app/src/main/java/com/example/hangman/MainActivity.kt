package com.example.hangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import kotlin.random.Random
import android.widget.Toast
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button


class MainActivity : AppCompatActivity() {
    // Word to find
    private var wordToFind: String? = null
    // Word found stored in a char array to show progression of user
    private var wordFound: CharArray? = null
    private var nbErrors: Int = 0
    // letters already entered by user
    private val letters = ArrayList<String>()
    private var img: ImageView? = null
    private var wordTv: TextView? = null
    private var wordToFindTv: TextView? = null
    private var guessedLetterText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        img = findViewById(R.id.img)
        wordTv = findViewById(R.id.wordTv)
        wordToFindTv = findViewById(R.id.wordToFindTv)
        guessedLetterText = findViewById(R.id.guessedLetters)
        newGame()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.new_game) {
            newGame()
        }

        return super.onOptionsItemSelected(item)
    }

    // Method returning randomly next word to find
    private fun nextWordToFind(): String {
        return WORDS[Random.nextInt(WORDS.size)]
    }

    // Method for starting a new game
    private fun newGame() {
        nbErrors = -1
        letters.clear()
        wordToFind = nextWordToFind()

        // word found initialization
        wordFound = CharArray(wordToFind!!.length)

        for (i in wordFound!!.indices) {
            wordFound!![i] = '_'
        }

        updateImg(nbErrors)
        wordTv!!.text = wordFoundContent()
        wordToFindTv!!.text = ""
    }

    // Method returning trus if word is found by user
    private fun wordFound(): Boolean {
        return wordToFind!!.contentEquals(String(wordFound!!))
    }

    // Method updating the word found after user entered a character
    private fun enter(c: String) {
        // we update only if c has not already been entered
        if (!letters.contains(c)) {
            // we check if word to find contains c
            if (wordToFind!!.contains(c)) {
                // if so, we replace _ by the character c
                var index = wordToFind!!.indexOf(c)

                while (index >= 0) {
                    wordFound?.set(index, c[0])
                    index = wordToFind!!.indexOf(c, index + 1)

                }
            } else {
                // c not in the word => error
                nbErrors++
                Toast.makeText(this, R.string.try_an_other, Toast.LENGTH_SHORT).show()
            }
            guessedLetterText!!.append(c)

            // c is now a letter entered
            letters.add(c)
        } else {
            Toast.makeText(this, R.string.letter_already_entered, Toast.LENGTH_SHORT).show()
        }
    }

    // Method returning the state of the word found by the user until by now
    private fun wordFoundContent(): String {
        val builder = StringBuilder()

        for (i in wordFound!!.indices) {
            builder.append(wordFound!![i])

            if (i < wordFound!!.size - 1) {
                builder.append(" ")
            }
        }

        return builder.toString()
    }


    private fun updateImg(play: Int) {
        val resImg = resources.getIdentifier(
            "hangman_$play", "drawable",
            packageName
        )
        img!!.setImageResource(resImg)
    }


    fun touchLetter(v: View) {
        if (nbErrors < MAX_ERRORS && getString(R.string.you_win) != wordToFindTv!!.text) {
            val letter = (v as Button).text.toString()
            enter(letter)
            wordTv!!.text = wordFoundContent()
            updateImg(nbErrors)

            // check if word is found
            if (wordFound()) {
                Toast.makeText(this, R.string.you_win, Toast.LENGTH_SHORT).show()
                wordToFindTv!!.setText(R.string.you_win)
            } else {
                if (nbErrors >= MAX_ERRORS) {
                    Toast.makeText(this, R.string.you_lose, Toast.LENGTH_SHORT).show()
                    wordToFindTv!!.text =
                        getString(R.string.word_to_find).replace("#word#", wordToFind!!)
                }
            }
        } else {
            Toast.makeText(this, R.string.game_is_ended, Toast.LENGTH_SHORT).show()
        }
    }

    companion object  {

        // Java Keywords
        val WORDS = arrayOf(
            "ABSTRACT",
            "ASSERT",
            "BOOLEAN",
            "BREAK",
            "BYTE",
            "CASE",
            "CATCH",
            "CHAR",
            "CLASS",
            "CONST",
            "CONTINUE",
            "DEFAULT",
            "DOUBLE",
            "DO",
            "ELSE",
            "ENUM",
            "EXTENDS",
            "FALSE",
            "FINAL",
            "FINALLY",
            "FLOAT",
            "FOR",
            "GOTO",
            "IF",
            "IMPLEMENTS",
            "IMPORT",
            "INSTANCEOF",
            "INT",
            "INTERFACE",
            "LONG",
            "NATIVE",
            "NEW",
            "NULL",
            "PACKAGE",
            "PRIVATE",
            "PROTECTED",
            "PUBLIC",
            "RETURN",
            "SHORT",
            "STATIC",
            "STRICTFP",
            "SUPER",
            "SWITCH",
            "SYNCHRONIZED",
            "THIS",
            "THROW",
            "THROWS",
            "TRANSIENT",
            "TRUE",
            "TRY",
            "VOID",
            "VOLATILE",
            "WHILE"
        )

        // Max errors before user lose
        const val MAX_ERRORS = 6
    }
}
