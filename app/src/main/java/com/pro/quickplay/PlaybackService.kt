package com.pro.quickplay


import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService : MediaSessionService() {

    @Inject
    lateinit var exoPlayer: ExoPlayer
    private var mediaSession: MediaSession? = null

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSession
            .Builder(this, exoPlayer)
            .build()


        /*this.setMediaNotificationProvider(object : MediaNotification.Provider {
            override fun createNotification(
                mediaSession: MediaSession,
                mediaButtonPreferences: ImmutableList<CommandButton>,
                actionFactory: MediaNotification.ActionFactory,
                onNotificationChangedCallback: MediaNotification.Provider.Callback
            ): MediaNotification {



                val notification = NotificationCompat.Builder(this@PlaybackService, CHANNEL_ID)
                    .setContentTitle("Hello Guys")
                    .setContentText("Hello Izaz")
                    .setSmallIcon(R.drawable.app_icon)
                    .setStyle(MediaStyleNotificationHelper.MediaStyle(mediaSession))
                    .build()

                startForeground(1, notification)
                return MediaNotification(1, notification)
            }



            override fun handleCustomCommand(
                session: MediaSession,
                action: String,
                extras: Bundle
            ): Boolean {
                return false
            }






        })*/


    }


    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession


    @OptIn(UnstableApi::class)
    override fun onTaskRemoved(rootIntent: Intent?) {

        val player = mediaSession?.player

        if (player != null) {
            if (!player.playWhenReady
                || player.mediaItemCount == 0
                || player.playbackState == Player.STATE_IDLE
            ) {
                stopSelf()
                releaseResources()
            }
        }
    }


    @OptIn(UnstableApi::class)
    override fun onDestroy() {
        stopSelf()
        releaseResources()
        super.onDestroy()
    }

    private fun releaseResources() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
    }


}