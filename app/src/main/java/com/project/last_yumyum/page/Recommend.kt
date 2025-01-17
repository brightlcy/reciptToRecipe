package com.project.last_yumyum.page

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.last_yumyum.Dataclass.MainPage
import com.project.last_yumyum.R
import com.project.last_yumyum.retrofitClient.RetrofitClient

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Recommend : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recommend)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 하단바 페이지 변경
        setupBottomNavigation()
        val listView: ListView = findViewById(R.id.listViewRecipes)
        fetchRecommendedRecipes(listView)
    }

    private fun fetchRecommendedRecipes(listView: ListView) {
        RetrofitClient.api.recommendRecipes().enqueue(object : Callback<List<MainPage>> {
            override fun onResponse(
                call: Call<List<MainPage>>,
                response: Response<List<MainPage>>
            ) {
                if (response.isSuccessful) {
                    val recipes = response.body()
                    if (recipes != null) {
                        val recipeNames = recipes.map { it.rcpName }
                        val recipeids = recipes.map { it.rcpId }
                        listView.adapter = ArrayAdapter(
                            this@Recommend,
                            android.R.layout.simple_list_item_1,
                            recipeNames
                        )

                        listView.setOnItemClickListener { parent, view, position, id ->
                            val selectedRecipeId = recipeids[position]
                            val intent = Intent(this@Recommend, recipe_detail::class.java)
                            intent.putExtra("RECIPE_ID", selectedRecipeId)
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(this@Recommend, "추천 레시피를 불러올 수 없습니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this@Recommend, "서버 응답이 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MainPage>>, t: Throwable) {
                Toast.makeText(this@Recommend, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    ////////////////////////하단바 페이지변경
    private fun setupBottomNavigation() {
        val transitionHomeButton =
            findViewById<RelativeLayout>(R.id.home_button) // RelativeLayout으로 변경
        transitionHomeButton.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }

        val transitionListItemButton = findViewById<RelativeLayout>(R.id.list_button)
        transitionListItemButton.setOnClickListener {
            val intent = Intent(this, RetainedIngredient::class.java)
            startActivity(intent)
        }
        val transitionPictureAddButton = findViewById<RelativeLayout>(R.id.camera_button)
        transitionPictureAddButton.setOnClickListener {
            val intent = Intent(this, PictureAdd::class.java)
            startActivity(intent)
        }

        val transitionSearchButton = findViewById<RelativeLayout>(R.id.search_button)
        transitionSearchButton.setOnClickListener {
            val intent = Intent(this, SearchRecipe::class.java)
            startActivity(intent)
        }

        val transitionRecommendButton = findViewById<RelativeLayout>(R.id.recommend_button)
        transitionRecommendButton.setOnClickListener {
            val intent = Intent(this, Recommend::class.java)
            startActivity(intent)
        }
    }
}
