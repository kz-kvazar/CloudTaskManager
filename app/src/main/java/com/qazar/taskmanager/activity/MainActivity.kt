package com.qazar.taskmanager.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.qazar.taskmanager.FirebaseDAO
import com.qazar.taskmanager.compose.MainView
import com.qazar.taskmanager.R
import com.qazar.taskmanager.Task
import com.qazar.taskmanager.ui.theme.TaskManagerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private var mainView: MainView = MainView()

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    private lateinit var taskSet: List<Task>
    // [END declare_auth]

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    //mainView = MainView()
                    mainView.Preview(R.drawable.task, R.drawable.delete, R.drawable.cloud_done, R.drawable.cloud_off)
                }
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id)).requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // [END config_signin]

        // [START initialize_auth]
        val signInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        val account = task.getResult(ApiException::class.java)
                        firebaseAuthWithGoogle(account.idToken!!)
                    } catch (e: ApiException) {
                        // Обработка ошибки аутентификации
                        Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(applicationContext, "Hello Anonymous", Toast.LENGTH_SHORT).show()
            }
        }

        // Initialize Firebase Auth
        auth = Firebase.auth

        val currentUser = auth.currentUser
        if (currentUser == null) {
            val signInIntent: Intent = googleSignInClient.signInIntent
            signInLauncher.launch(signInIntent)
        } else {
            dao(currentUser)
        }

    }
    // [START on_start_check_user]

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val user = auth.currentUser
                user?.let { dao(it) }
            } else {
                // If sign in fails, display a message to the user.
            }
        }
    }
    // [END auth_with_google]

    fun dao(user: FirebaseUser) {
        Toast.makeText(applicationContext, "Hello + ${user.displayName}", Toast.LENGTH_LONG).show()

        val firebaseDAO = FirebaseDAO(user.uid)
        firebaseDAO.connect(applicationContext)


        val coroutineScopeMain = CoroutineScope(Dispatchers.Main)
        mainView.firebaseDAO = firebaseDAO

        coroutineScopeMain.launch {
            firebaseDAO.addTask(Task(1, "now", " current Task", false))
            firebaseDAO.addTask(Task(1, "wow", " wow Task", false))
            firebaseDAO.addTask(Task(1, "lol", " lol Task", false))

            taskSet = coroutineScopeMain.async {
                firebaseDAO.getAllTask()
            }.await()

            Toast.makeText(applicationContext, taskSet.size.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
