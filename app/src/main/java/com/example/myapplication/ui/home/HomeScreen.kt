package com.example.myapplication.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.NoteItem
import com.example.myapplication.ui.theme.MyApplicationTheme


@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
    onNavigateToAddNote: () -> Unit,
    onNavigateToEditNote: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onAddClick = onNavigateToAddNote,
        onNoteClick = onNavigateToEditNote
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    onAddClick: () -> Unit,
    onNoteClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notes Mini", style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAddClick()
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FilterView(
                uiState = uiState,
                onEvent = onEvent,
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(36.dp),
                        color = Color.Green,
                        strokeWidth = 4.dp
                    )
                }
            } else if (uiState.filterList.isEmpty()) {
                EmptyState()
            } else {
                //item list
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = uiState.filterList,
                        key = { it.id }
                    ) { note ->
                        SingleNote(
                            note = note,
                            onFavorChange = {
                                onEvent(HomeUiEvent.OnFavorChange(itemId = note.id))
                            },
                            onDelete = { onEvent(HomeUiEvent.OnDeleteNoteClick(note.id)) },
                            modifier = Modifier.clickable {
                                onNoteClick(note.id)
                            }
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun SingleNote(
    note: NoteItem,
    onFavorChange: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(
                width = 1.dp, color = Color(0xFFD7DEE8), shape = RoundedCornerShape(12.dp)
            )
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = {
                onFavorChange()
            }
        ) {
            Icon(
                imageVector = if (note.isFavor) Icons.Default.StarRate else Icons.Default.StarBorder,
                contentDescription = null,
                tint = if (note.isFavor) Color(0xFFFFD700) else Color.Gray
            )
        }

        IconButton(
            onClick = { onDelete() }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = Color.Red
            )
        }
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No notes match this filter",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Try switching the filter or add a new note",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp
                )
            )
        }
    }
}


@Composable
fun FilterView(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp, color = Color(0xFFD7DEE8), shape = RoundedCornerShape(14.dp)
            )
    ) {
        SingleFilter(
            title = "All",
            isFilterSelected = uiState.currentFilter == FilterList.ALL,
            onButtonSelected = {
                onEvent(HomeUiEvent.OnFilterChanged(FilterList.ALL))
            },
            modifier = Modifier.weight(1f)
        )

        SingleFilter(
            title = "UnFavor",
            isFilterSelected = uiState.currentFilter == FilterList.UNFAVOR,
            onButtonSelected = {
                onEvent(HomeUiEvent.OnFilterChanged(FilterList.UNFAVOR))
            },
            modifier = Modifier.weight(1f)
        )

        SingleFilter(
            title = "Favorite",
            isFilterSelected = uiState.currentFilter == FilterList.FAVORITE,
            onButtonSelected = {
                onEvent(HomeUiEvent.OnFilterChanged(FilterList.FAVORITE))
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SingleFilter(
    title: String,
    isFilterSelected: Boolean,
    onButtonSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isFilterSelected) Color(0xFFE8F1FF) else Color.Transparent
            )
            .clickable { onButtonSelected() }
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isFilterSelected) Color(0xFF3B82F6) else Color(0xFF5F6368)
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun HomePreview() {
    MyApplicationTheme {
        HomeScreen(
            uiState = HomeUiState(
                noteList = listOf(
                    NoteItem(id = 1, title = "Note 1", content = "Content 1", isFavor = false),
                    NoteItem(
                        id = 2,
                        title = "Note 2",
                        content = "Lorem is pum abc bacb bacb bacb bacsb bbascbb babsc bbascbbbbascbbbbasbcbbasbdabsd bdhashdb basbdnbhjbabsd jhasd",
                        isFavor = true
                    ),
                    NoteItem(id = 3, title = "Note 3", content = "Content 3", isFavor = false),
                ),
                currentFilter = FilterList.ALL,
                isLoading = false
            ),
            onEvent = {},
            onAddClick = {},
            onNoteClick = {}
        )
    }
}
