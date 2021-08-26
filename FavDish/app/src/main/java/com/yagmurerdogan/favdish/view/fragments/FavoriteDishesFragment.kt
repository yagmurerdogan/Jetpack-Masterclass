package com.yagmurerdogan.favdish.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.yagmurerdogan.favdish.application.FavDishApplication
import com.yagmurerdogan.favdish.databinding.FragmentFavoriteDishesBinding
import com.yagmurerdogan.favdish.model.entities.FavDish
import com.yagmurerdogan.favdish.view.activities.MainActivity
import com.yagmurerdogan.favdish.view.adapters.FavDishAdapter
import com.yagmurerdogan.favdish.viewmodel.FavDishViewModel
import com.yagmurerdogan.favdish.viewmodel.FavDishViewModelFactory

class FavoriteDishesFragment : Fragment() {

    private var mBinding: FragmentFavoriteDishesBinding? = null

    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentFavoriteDishesBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFavDishViewModel.favoriteDishes.observe(viewLifecycleOwner) { dishes ->
            dishes.let {

                mBinding!!.rvFavoriteDishesList.layoutManager =
                    GridLayoutManager(requireActivity(), 2)
                val adapter = FavDishAdapter(this)
                mBinding!!.rvFavoriteDishesList.adapter = adapter

                if (it.isNotEmpty()) {
                    mBinding!!.rvFavoriteDishesList.visibility = View.VISIBLE
                    mBinding!!.tvNoFavoriteDishesAvailable.visibility = View.GONE
                    adapter.dishesList(it)
                } else {
                    mBinding!!.rvFavoriteDishesList.visibility = View.GONE
                    mBinding!!.tvNoFavoriteDishesAvailable.visibility = View.VISIBLE
                }
            }
        }
    }

    fun dishDetails(favDish: FavDish) {
        findNavController().navigate(
            FavoriteDishesFragmentDirections.actionFavoriteDishesToDishDetails(
                favDish
            )
        )

        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }
}