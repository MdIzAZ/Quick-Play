package com.pro.quickplay.presentation

import android.Manifest
import android.app.PictureInPictureParams
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.pro.quickplay.presentation.components.ShowSettingsRedirectDialog
import com.pro.quickplay.presentation.homescreen.HomeViewModel
import com.pro.quickplay.presentation.navigation.NavGraph
import com.pro.quickplay.presentation.theme.QuickPlayTheme
import com.pro.quickplay.presentation.video_screen.VideoViewModel
import dagger.hilt.android.AndroidEntryPoint

@UnstableApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private var videoViewBounds = Rect()
    private var currentRoute = ""
    private var isInLockMode = mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            var isPermissionGranted by remember { mutableStateOf(false) }
            val permissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                rememberPermissionState(Manifest.permission.READ_MEDIA_VIDEO)
            } else {
                rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            var shouldShowSettingDialog by remember { mutableStateOf(false) }


            val homeViewModel = hiltViewModel<HomeViewModel>()
            val videoViewModel = hiltViewModel<VideoViewModel>()


            LaunchedEffect(permissionState.status) {
                if (!permissionState.status.isGranted) {
                    if (!permissionState.status.shouldShowRationale) {
                        permissionState.launchPermissionRequest()
                    } else shouldShowSettingDialog = true
                } else {
                    isPermissionGranted = true
                    homeViewModel.refreshVideoItems()
                    shouldShowSettingDialog = false
                }
            }


            QuickPlayTheme {

                val navController = rememberNavController()


                LaunchedEffect(navController.currentBackStack) {
                    navController.currentBackStackEntryFlow.collect {
                        currentRoute = it.destination.route ?: ""
                    }

                }


                if (shouldShowSettingDialog) ShowSettingsRedirectDialog(
                    context = this@MainActivity,
                    onDismissDialog = {
                        shouldShowSettingDialog = false
                    }
                )

                if (!isPermissionGranted) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            OutlinedButton(
                                onClick = {
                                    shouldShowSettingDialog = true
                                },
                                content = {
                                    Text(text = "Grant Permission")
                                }
                            )
                        }

                    }
                } else {

                    NavGraph(
                        navController = navController,
                        homeViewModel = homeViewModel,
                        videoViewModel = videoViewModel,
                        isInLockMode = isInLockMode.value,
                        onLockBtnClick = {
                            isInLockMode.value = false
                        }
                    )

                }
            }
        }

    }



    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (currentRoute == "com.pro.quickplay.presentation.navigation.Routes.VideoScreen/{videoPath}") enterPip()
        else return
    }




    private fun enterPip() {
        val aspectRation = Rational(16, 9)
        isInLockMode.value = true
        val params = PictureInPictureParams.Builder()
            .setAspectRatio(aspectRation)
            .setSeamlessResizeEnabled(true)
            .build()
        enterPictureInPictureMode(params)
    }

}

