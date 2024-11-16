package me.austinng.servicelocatordemo.ui.screen.newnote

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NewNoteScreen(
    viewModel: NewNoteViewModel,
    onNoteSaved: () -> Unit,
    onBackClicked: () -> Unit
) {
    val note by viewModel.note.collectAsState()
    val isNoteSaved by viewModel.isNoteSaved.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val (contentFocusRequester) = FocusRequester.createRefs()

    LaunchedEffect(isNoteSaved) {
        if (isNoteSaved) {
            onNoteSaved()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.ArrowBack, null, modifier = Modifier.clickable { onBackClicked() })
            Text(
                text = "New note",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 32.dp, bottom = 32.dp, start = 16.dp)
            )
        }

        // Title TextField
        OutlinedTextField(
            value = note.title,
            onValueChange = { viewModel.updateTitle(it) },
            label = { Text("Title") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                contentFocusRequester.requestFocus()
            }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Content TextField
        OutlinedTextField(
            value = note.content,
            onValueChange = { viewModel.updateContent(it) },
            label = { Text("Content") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .focusRequester(focusRequester = contentFocusRequester)
        )

        // Save Button
        Button(
            onClick = {
                viewModel.saveNote()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}