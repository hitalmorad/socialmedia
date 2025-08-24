package com.loop.socialmedia.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.loop.socialmedia.data.model.MediaType
import com.loop.socialmedia.data.model.Post
import com.loop.socialmedia.data.model.User
import com.loop.socialmedia.data.repository.CloudinaryRepository
import com.loop.socialmedia.data.repository.UserRepository
import kotlinx.coroutines.launch
import java.util.UUID
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostEditorScreen(
    navController: NavController,
    viewModel: PostEditorViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state by viewModel.uiState.collectAsState()

    // Media picker launcher
    val mediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        viewModel.addMediaUris(uris)
    }

    // Location picker (simulated with text input)
    var showLocationDialog by rememberSaveable { mutableStateOf(false) }

    // Music picker (simulated)
    var showMusicDialog by rememberSaveable { mutableStateOf(false) }

    // Sticker picker (simulated)
    var showStickerDialog by rememberSaveable { mutableStateOf(false) }

    // Tag people dialog
    var showTagDialog by rememberSaveable { mutableStateOf(false) }

    // Schedule picker
    var showScheduleDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                onBackClick = {
                    if (state.hasContent) {
                        viewModel.showDiscardDialog(true)
                    } else {
                        navController.navigate("home") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                },
                onSaveDraft = { viewModel.saveDraft() }
            )
        },
        bottomBar = {
            ActionBar(
                onDiscard = { viewModel.showDiscardDialog(true) },
                onPreview = {
                    viewModel.previewPost { post ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("preview_post", post)
                        navController.navigate("post_preview")
                    }
                },
                onPost = {
                    scope.launch {
                        viewModel.uploadPost(context) { success ->
                            if (success) {
                                navController.navigate("home") {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            ContentArea(
                state = state,
                onMediaClick = { mediaLauncher.launch("image/*,video/*") },
                onTextChange = { viewModel.updateCaption(it) },
                onTagPeople = { showTagDialog = true },
                onAddLocation = { showLocationDialog = true },
                onAddMusic = { showMusicDialog = true },
                onAddSticker = { showStickerDialog = true },
                onGenerateAICaption = { viewModel.generateAICaption() },
                onPrivacyChange = { viewModel.updatePrivacy(it) },
                onFormatChange = { viewModel.updatePostFormat(it) },
                onSchedule = { showScheduleDialog = true }
            )

            // Discard Dialog
            AnimatedVisibility(
                visible = state.showDiscardDialog,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                AlertDialog(
                    onDismissRequest = { viewModel.showDiscardDialog(false) },
                    title = { Text("Discard Post?") },
                    text = { Text("Are you sure you want to discard this post?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.clearState()
                                navController.navigate("home") {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        ) { Text("Discard") }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.showDiscardDialog(false) }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // Tag People Dialog
            AnimatedVisibility(
                visible = showTagDialog,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                TagPeopleDialog(
                    viewModel = viewModel,
                    onDismiss = { showTagDialog = false }
                )
            }

            // Location Dialog
            AnimatedVisibility(
                visible = showLocationDialog,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                LocationDialog(
                    onLocationSelected = { location ->
                        viewModel.updateLocation(location)
                        showLocationDialog = false
                    },
                    onDismiss = { showLocationDialog = false }
                )
            }

            // Music Dialog
            AnimatedVisibility(
                visible = showMusicDialog,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                MusicDialog(
                    onMusicSelected = { music ->
                        viewModel.updateMusic(music)
                        showMusicDialog = false
                    },
                    onDismiss = { showMusicDialog = false }
                )
            }

            // Sticker Dialog
            AnimatedVisibility(
                visible = showStickerDialog,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                StickerDialog(
                    onStickerSelected = { sticker ->
                        viewModel.updateSticker(sticker)
                        showStickerDialog = false
                    },
                    onDismiss = { showStickerDialog = false }
                )
            }

            // Schedule Dialog
            AnimatedVisibility(
                visible = showScheduleDialog,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                ScheduleDialog(
                    onScheduleSelected = { timestamp ->
                        viewModel.updateSchedule(timestamp)
                        showScheduleDialog = false
                    },
                    onDismiss = { showScheduleDialog = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onBackClick: () -> Unit, onSaveDraft: () -> Unit) {
    TopAppBar(
        title = { Text("Create Post", style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.primary)
            }
        },
        actions = {
            TextButton(onClick = onSaveDraft) {
                Text("Save Draft", color = MaterialTheme.colorScheme.primary)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun ContentArea(
    state: PostEditorState,
    onMediaClick: () -> Unit,
    onTextChange: (TextFieldValue) -> Unit,
    onTagPeople: () -> Unit,
    onAddLocation: () -> Unit,
    onAddMusic: () -> Unit,
    onAddSticker: () -> Unit,
    onGenerateAICaption: () -> Unit,
    onPrivacyChange: (String) -> Unit,
    onFormatChange: (MediaType) -> Unit,
    onSchedule: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Media Upload Zone
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                .clickable { onMediaClick() }
                .pointerInput(Unit) {
                    detectDragGestures { _, _ -> onMediaClick() }
                }
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (state.mediaUris.isEmpty()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Media",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            "Add Photo/Video",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    AsyncImage(
                        model = state.mediaUris.first(),
                        contentDescription = "Media Preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        // Thumbnail Preview Strip
        AnimatedVisibility(
            visible = state.mediaUris.size > 1,
            enter = slideInVertically(animationSpec = tween(300)) + fadeIn(),
            exit = slideOutVertically(animationSpec = tween(300)) + fadeOut()
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.mediaUris) { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = "Thumbnail",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        // Text/Caption Box
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            BasicTextField(
                value = state.caption,
                onValueChange = onTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(120.dp),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp
                ),
                decorationBox = { innerTextField ->
                    if (state.caption.text.isEmpty()) {
                        Text(
                            "Write something...",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    innerTextField()
                }
            )
        }

        // Enhancements
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            EnhancementButton(Icons.Default.People, "Tag People", onTagPeople)
            EnhancementButton(Icons.Default.LocationOn, "Add Location", onAddLocation)
            EnhancementButton(Icons.Default.MusicNote, "Add Music", onAddMusic)
            EnhancementButton(Icons.Default.Mood, "Add Sticker", onAddSticker)
            EnhancementButton(Icons.Default.AutoAwesome, "AI Caption", onGenerateAICaption)
        }

        // Tagged Users
        AnimatedVisibility(
            visible = state.taggedUsers.isNotEmpty(),
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.taggedUsers) { userId ->
                    Text(
                        text = "@${state.taggedUsernames[userId] ?: userId}",
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Friendship Score Indicator
        AnimatedVisibility(
            visible = state.taggedUsers.isNotEmpty(),
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            LinearProgressIndicator(
                progress = state.friendshipScore / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }

        // Privacy Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PrivacyOption("Public", state.privacy == "Public") { onPrivacyChange("Public") }
            PrivacyOption("Friends", state.privacy == "Friends") { onPrivacyChange("Friends") }
            PrivacyOption("Private", state.privacy == "Private") { onPrivacyChange("Private") }
        }

        // Post Format Switcher
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FormatOption("Post", state.postFormat == MediaType.POST) { onFormatChange(MediaType.POST) }
            FormatOption("Story", state.postFormat == MediaType.STORY) { onFormatChange(MediaType.STORY) }
            FormatOption("Reel", state.postFormat == MediaType.REEL) { onFormatChange(MediaType.REEL) }
        }

        // Schedule Option
        TextButton(
            onClick = onSchedule,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(
                text = if (state.scheduledTime > 0) "Scheduled: ${SimpleDateFormat("MMM dd, yyyy HH:mm").format(Date(state.scheduledTime))}" else "Schedule Post",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun EnhancementButton(icon: androidx.compose.ui.graphics.vector.ImageVector, contentDescription: String, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Icon(icon, contentDescription = contentDescription, tint = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun PrivacyOption(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun FormatOption(text: String, isSelected: Boolean, onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(
            text = text,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun TagPeopleDialog(viewModel: PostEditorViewModel, onDismiss: () -> Unit) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var searchResults by rememberSaveable { mutableStateOf<List<User>>(emptyList()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tag People") },
        text = {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        scope.launch {
                            viewModel.searchUsers(it).onSuccess { users ->
                                searchResults = users
                            }
                        }
                    },
                    label = { Text("Search users") },
                    modifier = Modifier.fillMaxWidth()
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(searchResults) { user ->
                        Text(
                            text = "@${user.username}",
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                .clickable { viewModel.addTaggedUser(user.id, user.username) }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Done") }
        }
    )
}

@Composable
fun LocationDialog(onLocationSelected: (String) -> Unit, onDismiss: () -> Unit) {
    var location by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Location") },
        text = {
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Enter location") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = {
                if (location.isNotBlank()) onLocationSelected(location)
                onDismiss()
            }) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun MusicDialog(onMusicSelected: (String) -> Unit, onDismiss: () -> Unit) {
    val musicOptions = listOf("Song 1", "Song 2", "Song 3")
    var selectedMusic by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Music") },
        text = {
            Column {
                musicOptions.forEach { music ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedMusic = music }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedMusic == music,
                            onClick = { selectedMusic = music }
                        )
                        Text(music, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (selectedMusic.isNotBlank()) onMusicSelected(selectedMusic)
                onDismiss()
            }) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun StickerDialog(onStickerSelected: (String) -> Unit, onDismiss: () -> Unit) {
    val stickers = listOf("😊", "🎉", "🔥")
    var selectedSticker by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Sticker") },
        text = {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(stickers) { sticker ->
                    Text(
                        text = sticker,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { selectedSticker = sticker },
                        fontSize = 24.sp
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (selectedSticker.isNotBlank()) onStickerSelected(selectedSticker)
                onDismiss()
            }) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun ScheduleDialog(onScheduleSelected: (Long) -> Unit, onDismiss: () -> Unit) {
    var date by rememberSaveable { mutableStateOf("") }
    var time by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Schedule Post") },
        text = {
            Column {
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date (e.g., 2025-08-25)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time (e.g., 14:30)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                try {
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    val timestamp = sdf.parse("$date $time")?.time ?: return@TextButton
                    if (timestamp > System.currentTimeMillis()) {
                        onScheduleSelected(timestamp)
                        onDismiss()
                    }
                } catch (e: Exception) {
                    // Handle invalid date/time format
                }
            }) { Text("Schedule") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun ActionBar(onDiscard: () -> Unit, onPreview: () -> Unit, onPost: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onDiscard) {
            Icon(Icons.Default.Delete, contentDescription = "Discard", tint = MaterialTheme.colorScheme.error)
        }
        Button(
            onClick = onPost,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Post", fontSize = 16.sp)
        }
        IconButton(onClick = onPreview) {
            Icon(Icons.Default.Visibility, contentDescription = "Preview", tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@HiltViewModel
class PostEditorViewModel @Inject constructor(
    private val cloudinaryRepository: CloudinaryRepository,
    private val userRepository: UserRepository,
    private val firestore: FirebaseFirestore
) : ViewModel() {
    private val _uiState = MutableStateFlow(PostEditorState())
    val uiState: StateFlow<PostEditorState> = _uiState.asStateFlow()

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            userRepository.getCurrentUser()?.let { user ->
                _uiState.update { it.copy(currentUser = user) }
            }
        }
    }

    fun addMediaUris(uris: List<Uri>) {
        _uiState.update { it.copy(mediaUris = it.mediaUris + uris, hasContent = true) }
    }

    fun updateCaption(caption: TextFieldValue) {
        _uiState.update { it.copy(caption = caption, hasContent = caption.text.isNotEmpty() || it.mediaUris.isNotEmpty()) }
    }

    fun updateLocation(location: String) {
        _uiState.update { it.copy(location = location) }
    }

    fun updateMusic(music: String) {
        _uiState.update { it.copy(music = music) }
    }

    fun updateSticker(sticker: String) {
        _uiState.update { it.copy(sticker = sticker) }
    }

    fun updatePrivacy(privacy: String) {
        _uiState.update { it.copy(privacy = privacy) }
    }

    fun updatePostFormat(format: MediaType) {
        _uiState.update { it.copy(postFormat = format) }
    }

    fun updateSchedule(timestamp: Long) {
        _uiState.update { it.copy(scheduledTime = timestamp) }
    }

    fun addTaggedUser(userId: String, username: String) {
        _uiState.update {
            it.copy(
                taggedUsers = it.taggedUsers + userId,
                taggedUsernames = it.taggedUsernames + (userId to username),
                friendshipScore = it.friendshipScore + 10f
            )
        }
    }

    fun showDiscardDialog(show: Boolean) {
        _uiState.update { it.copy(showDiscardDialog = show) }
    }

    fun generateAICaption() {
        val suggestedCaption = "Having a blast! #FunTimes #${_uiState.value.location.takeIf { it.isNotBlank() }?.replace(" ", "") ?: "Adventure"}"
        _uiState.update { it.copy(caption = TextFieldValue(suggestedCaption)) }
    }

    fun saveDraft() {
        viewModelScope.launch {
            val state = _uiState.value
            val user = state.currentUser ?: return@launch
            val draft = Post(
                id = UUID.randomUUID().toString(),
                userId = user.id,
                userName = user.username,
                userProfileImage = user.profileImageUrl,
                content = state.caption.text,
                mediaUrls = emptyList(),
                mediaType = state.postFormat,
                hashtags = extractHashtags(state.caption.text),
                location = state.location,
                createdAt = System.currentTimeMillis()
            )
            firestore.collection("drafts")
                .document(draft.id)
                .set(draft)
                .await()
        }
    }

    fun previewPost(onPreview: (Post) -> Unit) {
        val state = _uiState.value
        val user = state.currentUser ?: return
        val post = Post(
            id = UUID.randomUUID().toString(),
            userId = user.id,
            userName = user.username,
            userProfileImage = user.profileImageUrl,
            content = state.caption.text,
            mediaUrls = state.mediaUris.map { it.toString() },
            mediaType = state.postFormat,
            hashtags = extractHashtags(state.caption.text),
            location = state.location,
            createdAt = if (state.scheduledTime > 0) state.scheduledTime else System.currentTimeMillis()
        )
        onPreview(post)
    }

    fun clearState() {
        _uiState.update { PostEditorState() }
    }

    suspend fun searchUsers(query: String): Result<List<User>> {
        return userRepository.searchUsers(query)
    }

    suspend fun uploadPost(context: Context, onComplete: (Boolean) -> Unit) {
        val state = _uiState.value
        val user = state.currentUser ?: return onComplete(false)

        val mediaUrls = mutableListOf<String>()
        state.mediaUris.forEach { uri ->
            val result = when (state.postFormat) {
                MediaType.STORY -> cloudinaryRepository.uploadStory(context, uri, user.id, isVideo = uri.toString().contains("video"))
                MediaType.REEL -> cloudinaryRepository.uploadReel(context, uri, user.id)
                else -> cloudinaryRepository.uploadPost(context, uri, user.id)
            }
            result.getOrNull()?.let { mediaUrls.add(it) }
        }

        if (mediaUrls.isNotEmpty() || state.caption.text.isNotEmpty()) {
            val collection = when (state.postFormat) {
                MediaType.STORY -> "stories"
                MediaType.REEL -> "reels"
                else -> "posts"
            }

            val post = Post(
                id = UUID.randomUUID().toString(),
                userId = user.id,
                userName = user.username,
                userProfileImage = user.profileImageUrl,
                content = state.caption.text,
                mediaUrls = mediaUrls,
                mediaType = state.postFormat,
                hashtags = extractHashtags(state.caption.text),
                location = state.location,
                createdAt = if (state.scheduledTime > 0) state.scheduledTime else System.currentTimeMillis()
            )

            firestore.collection(collection)
                .document(post.id)
                .set(post)
                .await()

            val userUpdate = when (state.postFormat) {
                MediaType.STORY -> mapOf("stories" to com.google.firebase.firestore.FieldValue.arrayUnion(post.id))
                MediaType.REEL -> mapOf("reels" to com.google.firebase.firestore.FieldValue.arrayUnion(post.id))
                else -> mapOf("posts" to com.google.firebase.firestore.FieldValue.arrayUnion(post.id))
            }
            firestore.collection("users")
                .document(user.id)
                .update(userUpdate)
                .await()

            if (state.taggedUsers.isNotEmpty()) {
                firestore.collection("users")
                    .document(user.id)
                    .update("friendshipScore", com.google.firebase.firestore.FieldValue.increment(state.taggedUsers.size * 10L))
                    .await()
            }

            onComplete(true)
        } else {
            onComplete(false)
        }
    }

    private fun extractHashtags(text: String): List<String> {
        return text.split(" ").filter { it.startsWith("#") }.map { it.removePrefix("#") }
    }
}

data class PostEditorState(
    val mediaUris: List<Uri> = emptyList(),
    val caption: TextFieldValue = TextFieldValue(),
    val taggedUsers: List<String> = emptyList(),
    val taggedUsernames: Map<String, String> = emptyMap(),
    val location: String = "",
    val music: String = "",
    val sticker: String = "",
    val privacy: String = "Public",
    val postFormat: MediaType = MediaType.POST,
    val friendshipScore: Float = 0f,
    val scheduledTime: Long = 0L,
    val showDiscardDialog: Boolean = false,
    val hasContent: Boolean = false,
    val currentUser: User? = null
)