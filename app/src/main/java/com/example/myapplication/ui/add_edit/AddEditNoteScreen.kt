package com.example.myapplication.ui.add_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme


@Composable
fun AddEditNoteRoute(
    viewModel: AddEditViewModel,
    noteId: Long,
    onBack: () -> Unit
) {
    val isEditMode = noteId != -1L

    val uiState by viewModel.uiState.collectAsState()
    AddEditNoteScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        isEditMote = isEditMode,
        onBackClick = onBack
    )
}

@Composable
fun AddEditNoteScreen(
    uiState: AddEditUiState,
    onEvent: (AddEditUiEvent) -> Unit,
    isEditMote: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = modifier.fillMaxSize(),

        ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isEditMote) "Edit Screen" else "Add Note Screen",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Title",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.inputTitle,
                onValueChange = {
                    onEvent(AddEditUiEvent.OnInputTitleChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    if (isEditMote) {
                        Text(
                            text = "Edit Title",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        Text(
                            text = "Add Title",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                },
                textStyle = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Content",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.inputContent,
                onValueChange = {
                    onEvent(AddEditUiEvent.OnInputContentChange(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .heightIn(max = 120.dp),
                label = {
                    if (isEditMote) {
                        Text(
                            text = "Edit Content",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        Text(
                            text = "Add Content",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                },
                textStyle = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (!isEditMote) {
                        onEvent(AddEditUiEvent.OnAddNote)
                        if (uiState.isSaveDone) {
                            keyboardController?.hide()
                            onBackClick()
                        }
                    } else {
                        onEvent(AddEditUiEvent.OnUpdateNote)
                    }

//                    onEvent(AddEditUiEvent.OnAddNote)
//                    keyboardController?.hide()
//                    onBackClick()
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isEditMote) "Save" else "+ Add",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
            }

        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun AddEditNotePreview() {
    MyApplicationTheme() {
        AddEditNoteScreen(
            uiState = AddEditUiState(

            ),
            onEvent = {},
            isEditMote = false,
            onBackClick = {}
        )
    }
}