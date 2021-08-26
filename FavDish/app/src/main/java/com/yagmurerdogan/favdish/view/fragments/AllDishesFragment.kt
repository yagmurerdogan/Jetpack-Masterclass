package com.yagmurerdogan.favdish.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.yagmurerdogan.favdish.R
import com.yagmurerdogan.favdish.application.FavDishApplication
import com.yagmurerdogan.favdish.databinding.DialogCustomListBinding
import com.yagmurerdogan.favdish.databinding.FragmentAllDishesBinding
import com.yagmurerdogan.favdish.model.entities.FavDish
import com.yagmurerdogan.favdish.utils.Constants
import com.yagmurerdogan.favdish.view.activities.AddUpdateDishActivity
import com.yagmurerdogan.favdish.view.activities.MainActivity
import com.yagmurerdogan.favdish.view.adapters.CustomListItemAdapter
import com.yagmurerdogan.favdish.view.adapters.FavDishAdapter
import com.yagmurerdogan.favdish.viewmodel.FavDishViewModel
import com.yagmurerdogan.favdish.viewmodel.FavDishViewModelFactory

class AllDishesFragment : Fragment() {

    private lateinit var mBinding: FragmentAllDishesBinding
    private lateinit var mFavDishAdapter: FavDishAdapter
    private lateinit var mCustomListDialog: Dialog

    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            FragmentAllDishesBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        mFavDishAdapter = FavDishAdapter(this)
        mBinding.rvDishesList.adapter = mFavDishAdapter

        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
            dishes.let {

                // TODO Step 9: Pass the dishes list to the adapter class.
                // START
                if (it.isNotEmpty()) {

                    mBinding.rvDishesList.visibility = View.VISIBLE
                    mBinding.tvNoDishesAddedYet.visibility = View.GONE

                    mFavDishAdapter.dishesList(it)
                } else {

                    mBinding.rvDishesList.visibility = View.GONE
                    mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                }
            }
        }
    }

    fun dishDetails(favDish: FavDish) {
        findNavController().navigate(
            AllDishesFragmentDirections.actionAllDishesToDetails(
                favDish
            )
        )

        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.hideBottomNavigationView()
        }
    }

    fun deleteDish(dish: FavDish) {
        val builder = AlertDialog.Builder(requireActivity())
        with(builder) {
            setTitle(resources.getString(R.string.title_delete_dish))
            setMessage(resources.getString(R.string.msg_delete_dish_dialog))
            setIcon(R.drawable.ic_alert)
            setPositiveButton(resources.getString(R.string.lbl_yes)) { dialogInterface, _ ->
                mFavDishViewModel.delete(dish)
                dialogInterface.dismiss()
            }
            setNegativeButton(resources.getString(R.string.lbl_no)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
        }


        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun filterDishesListDialog() {
        mCustomListDialog = Dialog(requireActivity())
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)

        mCustomListDialog.setContentView(binding.root)
        binding.tvTitle.text = resources.getString(R.string.title_select_item_to_filter)

        val dishTypes = Constants.dishTypes()
        dishTypes.add(0, Constants.ALL_ITEMS)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val adapter =
            CustomListItemAdapter(requireActivity(),this@AllDishesFragment, dishTypes, Constants.FILTER_SELECTION)
        binding.rvList.adapter = adapter
        mCustomListDialog.show()
    }

    override fun onResume() {
        super.onResume()

        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.showBottomNavigationView()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_add_dish -> {
                startActivity(Intent(requireActivity(), AddUpdateDishActivity::class.java))
                return true
            }
            R.id.action_filter_dishes -> {
                filterDishesListDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun filterSelection(filterItemSelection: String) {
        mCustomListDialog.dismiss()



        if (filterItemSelection == Constants.ALL_ITEMS) {
            mFavDishViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
                dishes.let {
                    if (it.isNotEmpty()) {
                        mBinding.rvDishesList.visibility = View.VISIBLE
                        mBinding.tvNoDishesAddedYet.visibility = View.GONE
                        mFavDishAdapter.dishesList(it)
                    } else {
                        mBinding.rvDishesList.visibility = View.GONE
                        mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                    }
                }
            }
        } else {
            mFavDishViewModel.getFilteredList(filterItemSelection).observe(viewLifecycleOwner) { dishes ->
                dishes.let {
                    if (it.isNotEmpty()) {
                        mBinding.rvDishesList.visibility = View.VISIBLE
                        mBinding.tvNoDishesAddedYet.visibility = View.GONE

                        mFavDishAdapter.dishesList(it)
                    } else {
                        mBinding.rvDishesList.visibility = View.GONE
                        mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}