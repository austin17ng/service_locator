package me.austinng.servicelocatordemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import me.austinng.servicelocatordemo.ui.screen.newnote.NewNoteDestination
import me.austinng.servicelocatordemo.ui.screen.newnote.NewNoteScreen
import me.austinng.servicelocatordemo.ui.screen.newnote.NewNoteViewModel
import me.austinng.servicelocatordemo.ui.screen.newnote.NewNoteViewModelFactory
import me.austinng.servicelocatordemo.ui.screen.notes.NotesDestination
import me.austinng.servicelocatordemo.ui.screen.notes.NotesScreen
import me.austinng.servicelocatordemo.ui.screen.notes.NotesViewModel
import me.austinng.servicelocatordemo.ui.screen.notes.NotesViewModelFactory
import me.austinng.servicelocatordemo.ui.theme.ServiceLocatorDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ServiceLocatorDemoTheme {
                val scope = rememberCoroutineScope()
                val snackBarHostState = remember { SnackbarHostState() }
                val navController = rememberNavController()
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
                ) { paddingValues ->

                    NavHost(
                        modifier = Modifier.padding(paddingValues),
                        navController = navController,
                        startDestination = NotesDestination.ROUTE,
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left, tween(300)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left, tween(300)
                            )
                        },
                        popEnterTransition = {

                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right, tween(300)
                            )
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right, tween(300)
                            )
                        }
                    ) {
                        composable(NotesDestination.ROUTE) {
                            val context = LocalContext.current.applicationContext
                            val noteRepository = ServiceLocator.provideNoteRepository(context)
                            val notesViewModel: NotesViewModel =
                                viewModel(factory = NotesViewModelFactory(noteRepository))
                            NotesScreen(
                                viewModel = notesViewModel,
                                onNewNoteClicked = {
                                    navController.navigate(NewNoteDestination.ROUTE)
                                }
                            )
                        }

                        composable(NewNoteDestination.ROUTE) {
                            val context = LocalContext.current.applicationContext
                            val noteRepository = ServiceLocator.provideNoteRepository(context)
                            val newNotesViewModel: NewNoteViewModel =
                                viewModel(factory = NewNoteViewModelFactory(noteRepository))
                            NewNoteScreen(
                                viewModel = newNotesViewModel,
                                onNoteSaved = {
                                    navController.popBackStack()
                                    scope.launch {
                                        snackBarHostState.showSnackbar("A new note created")
                                    }
                                },
                                onBackClicked = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}