package com.spacex

import com.spacex.model.FalconInfo


class SampleData {
    companion object {
        private const val mesageSize = 10;
        private val conversationSample: List<FalconInfo> = buildList<FalconInfo>(mesageSize) {
            for (i in 0 until mesageSize) {
                add(
                    FalconInfo(
                        name = "Name $i",
                        id = "Rocket $i",
                        description = "Detail can be long, Detail can be long, Detail can be long, Detail can be long, Detail can be long, Detail can be long, "
                    )
                )
            }
        }

        fun getRockets(): List<FalconInfo> {
            return conversationSample
        }
    }
}
