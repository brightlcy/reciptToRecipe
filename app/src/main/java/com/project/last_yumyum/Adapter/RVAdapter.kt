package com.project.last_yumyum.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.last_yumyum.Dataclass.MainPage
import com.project.last_yumyum.R
import com.project.last_yumyum.page.recipe_detail
import com.project.last_yumyum.retrofitClient.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RVAdapter(val context: Context, val List: MutableList<MainPage>) :
    RecyclerView.Adapter<RVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.rv_item1, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(List[position])

// 아이템 클릭 리스너 설정
        holder.recipeMore.setOnClickListener {
            val intent = Intent(context, recipe_detail::class.java)
            intent.putExtra("RECIPE_ID", List[position].rcpId)
            context.startActivity(intent)
        }

        // 즐겨찾기 버튼 클릭 리스너 설정
        holder.saveButton.setOnClickListener {
            val recipeId = List[position].rcpId
            if (recipeId != -1) {
                addRecipeToFavorites(recipeId)
            } else {
                Toast.makeText(context, "레시피를 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun getItemCount(): Int {
        return List.size
    }


    private fun addRecipeToFavorites(recipeId: Int) {
        RetrofitClient.api.addFavoriteRecipe(recipeId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "레시피가 찜 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "찜 목록 추가 실패: 서버 오류", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val saveButton: Button = itemView.findViewById(R.id.saveButton)
        val recipeMore: RelativeLayout = itemView.findViewById(R.id.recipeMore) // 추가
        fun bindItems(item: MainPage) {
            val rv_size = itemView.findViewById<TextView>(R.id.servingSize)
            val rv_diff = itemView.findViewById<TextView>(R.id.cookingDiff)
            val rv_time = itemView.findViewById<TextView>(R.id.cookingTime)
            val rv_title = itemView.findViewById<TextView>(R.id.cookingTitle)
            val rv_img = itemView.findViewById<ImageView>(R.id.rvImageArea)

            rv_title.text = item.rcpName
            rv_time.text = item.rcpCookingTime
            rv_diff.text = item.rcpDifficulty
            rv_size.text = item.rcpQuantities
            Glide.with(context)
                .load(item.rcpImageUrl)
                .into(rv_img)


        }
    }


}