package woowacourse.shopping.data.local.recentproduct

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecentProductDao {

    @Query("SELECT * FROM recent_products")
    suspend fun findAll(): List<String>

    @Insert
    suspend fun insert(item: RecentProductEntity)
}
