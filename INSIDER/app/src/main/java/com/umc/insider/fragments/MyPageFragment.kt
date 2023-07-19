package com.umc.insider.fragments

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.umc.insider.R
import com.umc.insider.adapter.ShoppingSaleAdapter
import com.umc.insider.auth.login.LogInActivity
import com.umc.insider.databinding.FragmentMyPageBinding
import com.umc.insider.model.SearchItem

class MyPageFragment : Fragment() {

    private var _binding : FragmentMyPageBinding? = null
    private val binding get() = _binding!!
    private val shoppingListAdapter = ShoppingSaleAdapter()
    private val saleListAdapter = ShoppingSaleAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)

        initView()

        return binding.root

    }

    private fun initView(){
        with(binding){
            val dummies = DummyDate()
            shoppingListRV.adapter = shoppingListAdapter
            shoppingListRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            shoppingListRV.addItemDecoration(ShoppingSaleListAdapterDecoration())
            shoppingListAdapter.submitList(dummies)

            saleListRV.adapter = saleListAdapter
            saleListRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            saleListRV.addItemDecoration(ShoppingSaleListAdapterDecoration())
            saleListAdapter.submitList(dummies)

            logoutTextView.setOnClickListener {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
                googleSignInClient.signOut()
                LoginManager.getInstance().logOut()
                startActivity(Intent(activity, LogInActivity::class.java))
                activity?.finish()
            }
        }
    }

    private fun DummyDate() : ArrayList<SearchItem>{
        val dummy1 = SearchItem(1, "양파1", "100g", "1000원")
        val dummy2 = SearchItem(2, "양파2", "200g", "2000원")
        val dummy3 = SearchItem(3, "양파3", "300g", "2800원")
        val dummy4 = SearchItem(4, "양파4", "400g", "3800원")
        val dummy5 = SearchItem(5, "양파5", "500g", "4500원")

        val arr = ArrayList<SearchItem>()
        arr.add(dummy1)
        arr.add(dummy2)
        arr.add(dummy3)
        arr.add(dummy4)
        arr.add(dummy5)

        return arr
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

class ShoppingSaleListAdapterDecoration : RecyclerView.ItemDecoration(){

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.right = 20
    }
}