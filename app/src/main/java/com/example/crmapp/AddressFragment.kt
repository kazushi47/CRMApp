package com.example.crmapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.crmapp.databinding.FragmentAddressBinding
import com.example.crmapp.db.dto.User
import com.google.android.material.snackbar.Snackbar

/**
 * ユーザ情報登録
 */
class AddressFragment : Fragment() {

    private var _binding: FragmentAddressBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    /** 入力データ格納用 */
    private var inputData: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* click buttonSubmit */
        binding.buttonSubmit.setOnClickListener {
            if (checkValues()) {
                /* TODO: insert data to DB */
                    showMessage("登録が完了しました")

                /* navigate self */
                findNavController().navigate(R.id.action_addressFragment_self)
            }
        }

        /* click buttonAddressSearch */
        binding.buttonAddressSearch.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * バリデーションチェック、エラー表示、登録可否判定
     * @return 登録可否
     */
    private fun checkValues(): Boolean {
        /* Postal code */
        val postalCode = binding.edittextPostalCode.text.toString()
        if (postalCode.isBlank() || postalCode.length != 7) {
            showMessage("適切な郵便番号を入力してください")
            return false
        }

        /* Prefecture */
        val prefecture = binding.spinnerPrefecture.selectedItem.toString()
        if (prefecture.isBlank()) {
            showMessage("都道府県を選択してください")
            return false
        }

        /* City */
        val city = binding.edittextCity.text.toString()
        if (city.isBlank()) {
            showMessage("適切な市町村を入力してください")
            return false
        }

        /* Address */
        val address = binding.edittextAddress.text.toString()
        if (address.isBlank()) {
            showMessage("適切な丁目番地を入力してください")
            return false
        }

        /* House name */
        val houseName = binding.edittextHouseName.text.toString()
        if (houseName.isBlank()) {
            showMessage("適切な建物名を入力してください")
            return false
        }

        /* Full name */
        val fullName = binding.edittextFullName.text.toString()
        if (fullName.isBlank()) {
            showMessage("適切な氏名を入力してください")
            return false
        }

        /* inputDataに格納 */
        inputData = User(postal_code = postalCode, prefecture = prefecture, city = city, address = address, house_name = houseName, full_name = fullName)

        return true
    }

    /**
     * メッセージを [Snackbar] で表示
     */
    private fun showMessage(message: String) {
        val view = activity?.findViewById<ViewGroup>(android.R.id.content)
        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()
    }
}