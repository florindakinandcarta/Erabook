import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.erabook.R
import com.example.erabook.data.TabNames
import com.example.erabook.databinding.FragmentObjectBinding
import com.example.erabook.fragments.home.tabFragments.NYTFragment
import com.example.erabook.util.ARG_TAB_NAME
import com.google.android.material.tabs.TabLayout

class ObjectFragment : Fragment() {
    private lateinit var binding: FragmentObjectBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var tabName: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentObjectBinding.inflate(layoutInflater, container, false)
        tabLayout = requireActivity().findViewById(R.id.tab_layout)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { args ->
            tabName = args.getString(ARG_TAB_NAME) ?: ""
            val fragment = when (tabName) {
//                "FOR_YOU" ->
                TabNames.NEW_YORK_BEST_SELLERS.toString() -> NYTFragment()
                else -> {
                    ObjectFragment()}
            }
            childFragmentManager.beginTransaction().replace(R.id.objectFragment, fragment).commit()
        }
    }
}