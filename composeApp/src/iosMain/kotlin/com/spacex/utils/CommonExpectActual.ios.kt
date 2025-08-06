package com.spacex.utils


import androidx.room.Room
import androidx.room.RoomDatabase
import com.spacex.database.AppDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSHomeDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

//actual fun shareLink(url: String) {
//    val currentViewController = UIApplication.sharedApplication().keyWindow?.rootViewController
//    val activityViewController = UIActivityViewController(listOf(url), null)
//    currentViewController?.presentViewController(
//        viewControllerToPresent = activityViewController,
//        animated = true,
//        completion = null
//    )
//}
//
//actual fun randomUUIDStr(): String {
//    return NSUUID().UUIDString()
//}
//
//actual fun getType(): Type {
//    return Type.Mobile
//}
//
//@OptIn(ExperimentalComposeUiApi::class)
//@Composable
//actual fun getScreenSize(): Size {
//    val configuration = LocalWindowInfo.current
//    val screenHeightDp = configuration.containerSize.height.dp
//    val screenWidthDP = configuration.containerSize.width.dp
//    return Size(width = screenWidthDP, height = screenHeightDp)
//}
//

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = documentDirectory() + "/$DB_Name"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}

