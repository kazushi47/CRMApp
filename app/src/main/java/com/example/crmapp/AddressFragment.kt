package com.example.crmapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.crmapp.databinding.FragmentAddressBinding
import com.example.crmapp.db.dao.PostalCodeDAO
import com.example.crmapp.db.dto.User
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.Callable
import java.util.concurrent.Executors

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

    /** DBテーブル postal_codes のDAO */
    private val postalCodeDAO = PostalCodeDAO()

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

        /* edit edittextPostalCode */
        binding.edittextPostalCode.doOnTextChanged { text, _, _, _ ->
            if (text?.length ?: 0 == 7) autoFillValues()
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
        if (postalCode.isNotBlank() && postalCode.length == 7) {
            val isPostalCodeAvailable = Executors.newSingleThreadExecutor().submit(Callable { postalCodeDAO.isPostalCodeAvailable(postalCode) }).get()
            if (!isPostalCodeAvailable) {
                showMessage("存在しない郵便番号です")
                return false
            }
        } else {
            showMessage("適切な郵便番号を入力してください")
            return false
        }

        /* Prefecture */
        val prefecture = binding.edittextPrefecture.text.toString()

        /* City */
        val city = binding.edittextCity.text.toString()
        if (city.isNotBlank() && Regex("""^[^\W\s　]+$""").matches(city)) {
            val isPostalCodeAndCityAvailable = Executors.newSingleThreadExecutor().submit(Callable { postalCodeDAO.isPostalCodeAndCityAvailable(postalCode, city) }).get()
            if (!isPostalCodeAndCityAvailable) {
                showMessage("郵便番号と市町村が対応していません")
                return false
            }
        } else {
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
     * 郵便番号が入力されたら住所を自動入力
     * （ただし、複数県の住所がある場合は最初にヒットした住所を自動入力する）
     */
    private fun autoFillValues() {
        val postalCode = binding.edittextPostalCode.text.toString()

        /* Prefecture */
        binding.edittextPrefecture.setText(Executors.newSingleThreadExecutor().submit(Callable { postalCodeDAO.findByPostalCode(postalCode).firstOrNull()?.prefecture }).get())

        /* City */
        binding.edittextCity.setText(Executors.newSingleThreadExecutor().submit(Callable { postalCodeDAO.findByPostalCode(postalCode).firstOrNull()?.city }).get())

        /* Address */
        var address = Executors.newSingleThreadExecutor().submit(Callable { postalCodeDAO.findByPostalCode(postalCode).firstOrNull()?.address }).get()
        // 「以下に掲載がない場合」は除く
        address = if (address == "以下に掲載がない場合") "" else address
        // 「（１～１９丁目）」という文字列は除く
        address = Regex("""（.*?）""").replace(address ?: "", "")
        binding.edittextAddress.setText(address)
    }

    /**
     * メッセージを [Snackbar] で表示
     */
    private fun showMessage(message: String) {
        val view = activity?.findViewById<ViewGroup>(android.R.id.content)
        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()
    }
}