import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
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


class TopFiveAdapter(val context: Context, val list: MutableList<MainPage>) :
    RecyclerView.Adapter<TopFiveAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(list[position])

        // 아이템 클릭 리스너 설정
        holder.itemView.setOnClickListener {
            val intent = Intent(context, recipe_detail::class.java)
            // 선택된 레시피의 rcpId를 인텐트에 추가하여 recipe_detail 액티비티로 전달
            intent.putExtra("RECIPE_ID", list[position].rcpId)
            context.startActivity(intent)
        }

        // 즐겨찾기 버튼 클릭 리스너 설정
        holder.favoriteButton.setOnClickListener {
            val recipeId = list[position].rcpId
            if (recipeId != -1) {
                addRecipeToFavorites(recipeId)
            } else {
                Toast.makeText(context, "레시피를 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
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
        val favoriteButton: Button = itemView.findViewById(R.id.favoriteButton)
        fun bindItems(item: MainPage) {
            val rvText = itemView.findViewById<TextView>(R.id.rvTextAre3)
            val rvImg = itemView.findViewById<ImageView>(R.id.rvImageArea)

            rvText.text = item.rcpName
            Glide.with(context)
                .load(item.rcpImageUrl)
                .into(rvImg)
        }
    }
}
