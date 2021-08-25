package com.yagmurerdogan.favdish.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.yagmurerdogan.favdish.R
import com.yagmurerdogan.favdish.databinding.FragmentDishDetailsBinding
import java.io.IOException
import java.util.*

class DishDetailsFragment : Fragment() {

    private var mBinding: FragmentDishDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentDishDetailsBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: DishDetailsFragmentArgs by navArgs()

        args.let {
            try {
                Glide.with(requireActivity())
                    .load(it.dishDetails.image)
                    .centerCrop()
                    .into(mBinding!!.ivDishImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            mBinding!!.tvTitle.text = it.dishDetails.title
            mBinding!!.tvType.text =
                it.dishDetails.type.capitalize(Locale.ROOT) // Used to make first letter capital
            mBinding!!.tvCategory.text = it.dishDetails.category
            mBinding!!.tvIngredients.text = it.dishDetails.ingredients
            mBinding!!.tvCookingDirection.text = it.dishDetails.directionToCook
            mBinding!!.tvCookingTime.text =
                resources.getString(R.string.lbl_estimate_cooking_time, it.dishDetails.cookingTime)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}