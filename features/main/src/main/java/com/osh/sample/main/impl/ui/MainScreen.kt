package com.osh.sample.main.impl.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.osh.sample.main.R
import com.osh.sample.main.impl.domain.RecordsItem
import com.osh.sample.main.impl.presentation.MainScreenViewModel
import com.osh.sample.main.impl.presentation.MainScreenViewModel.Event
import com.osh.sample.main.impl.presentation.MainScreenViewState
import com.osh.ui.compose.CustomTopAppBar
import com.osh.ui.compose.ListGridItem
import com.osh.ui.compose.MessageFullScreen
import com.osh.ui.compose.RenderList
import com.osh.ui.compose.SearchView
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ListScreen(
    model: MainScreenViewModel = hiltViewModel(),
    onNavigateDetails: (id: String) -> Unit = {},
) {
    LaunchedEffect(model) {
        model.eventsFlow.collectLatest { value ->
            when (value) {
                is Event.NavigateDetails -> onNavigateDetails(value.id)
            }
        }
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var text by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                titleText = "Discover",
                titleExpandedContent = {
                    SearchView(
                        value = text,
                        placeholderText = "Search",
                        onValueChange = {
                            text = it
                            model.onSearchChanged(it)
                        },
                        color = searchTextColor,
                        textStyle = MaterialTheme.typography.labelMedium.copy(color = searchTextColor),
                        placeholderTextStyle = MaterialTheme.typography.labelMedium.copy(color = searchTextColor),
                        bgColor = SearchBackground,
                        icon = {
                            when (model.state.isSearching) {
                                true -> CircularProgressIndicator(
                                    modifier = Modifier.size(
                                        24.dp,
                                        22.dp
                                    )
                                )
                                false -> Image(
                                    painter = painterResource(R.drawable.ic_search_icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp, 22.dp)
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp),
                    )
                },
                titleTextStyle = MaterialTheme.typography.titleSmall,
                titleExpandedTextStyle = MaterialTheme.typography.titleLarge,
                maxHeight = 192.dp,
                pinnedHeight = 88.dp,
                bottomPadding = 12.dp,
                startEndPadding = 16.dp,
                bgColor = SearchBackground,
                scrollBehavior = scrollBehavior,
            )
        },
        content = {
            when {
                model.state.isLoading -> RenderLoadingState(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                )
                model.state.lastError != null -> RenderErrorState(
                    error = model.state.lastError,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                )
                else -> RenderList(
                    modifier = Modifier
                        .padding(it)
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    items = model.state.mapToListItems(),
                    isPageLoadingInProgress = model.state.isPageLoadingInProgress,
                    totalItemsCount = model.state.items.size,
                    listItem = { item ->
                        RenderItems(item) { item ->
                            when (item) {
                                is ListItem.Data -> model.onItemClicked(item.data)
                                else -> {}
                            }
                        }
                    },
                    onPageUpdateRequired = model::onPageUpdateRequired
                )
            }
        }
    )
}


@Composable
fun RenderLoadingState(
    modifier: Modifier
) {
    MessageFullScreen(
        message = "Loading...",
        modifier = modifier
    )
}

@Composable
fun RenderErrorState(
    error: Throwable?,
    modifier: Modifier
) {
    MessageFullScreen(
        message = "Error - ${error?.message}",
        modifier = modifier
    )
}

internal sealed class ListItem(
    override val id: String,
    override val spanCount: Int,
) : ListGridItem {
    data class Data(val data: RecordsItem) : ListItem(data.id, 1)
    object Progress : ListItem("progress", 2)
}

internal fun MainScreenViewState.mapToListItems() = items.map { ListItem.Data(it) as ListItem }
    .toMutableList()
    .apply {
        if (this@mapToListItems.isPageLoadingInProgress) {
            add(ListItem.Progress)
        }
    }

@Composable
internal fun RenderItems(item: ListItem, onItemClickListener: (ListItem) -> Unit = {}) {
    when (item) {
        is ListItem.Data -> ImageCard(
            model = item.data,
            modifier = Modifier,
            onItemClickListener = { onItemClickListener(item) }
        )
        ListItem.Progress -> ProgressItem(modifier = Modifier.padding(8.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUnitApi::class)
@Composable
internal fun ImageCard(
    model: RecordsItem,
    modifier: Modifier = Modifier,
    onItemClickListener: (RecordsItem) -> Unit = {}
) {
    Card(
        modifier = modifier,
        onClick = { onItemClickListener(model) }
    ) {
        ConstraintLayout {
            val (image, counterOverlay, title, genres) = createRefs()
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(model.artworkUrl)
                    .build(),
                contentDescription = "model",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .drawWithCache {
                        val gradient = Brush.verticalGradient(
                            colors = listOf(ImageOverlayGradientTop, ImageOverlayGradientBottom),
                            startY = 0f,
                            endY = size.height
                        )
                        onDrawWithContent {
                            drawContent()
                            drawRect(gradient, blendMode = BlendMode.Multiply)
                        }
                    }
            )
            Box(
                modifier = Modifier
                    .constrainAs(counterOverlay) {
                        top.linkTo(parent.top, margin = 8.dp)
                        start.linkTo(parent.start, margin = 9.dp)
                    }
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(CounterBackground, RoundedCornerShape(CornerSize(9.dp)))
                )
                Row(
                    modifier = Modifier.padding(6.dp)
                ) {
                    Spacer(modifier = Modifier.width(3.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_listener_icon),
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = model.listenerCount.toString(),
                        fontSize = TextUnit(11f, TextUnitType.Sp),
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                }
            }

            Text(
                text = model.genres.joinToString(),
                fontSize = TextUnit(12f, TextUnitType.Sp),
                color = Color.White,
                maxLines = 1,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(genres) {
                        bottom.linkTo(parent.bottom, margin = 9.dp)
                        start.linkTo(parent.start, margin = 8.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                        width = Dimension.fillToConstraints
                    }
            )

            Text(
                text = model.trackTitle,
                fontSize = TextUnit(16f, TextUnitType.Sp),
                color = Color.White,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(title) {
                        bottom.linkTo(genres.top)
                        start.linkTo(parent.start, margin = 9.dp)
                        end.linkTo(parent.end, margin = 9.dp)
                        width = Dimension.fillToConstraints
                    }
            )
        }
    }
}

@Composable
internal fun ProgressItem(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .wrapContentSize()
        )
    }
}
