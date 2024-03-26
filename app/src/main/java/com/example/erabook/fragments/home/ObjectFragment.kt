import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.erabook.R
import com.example.erabook.databinding.FragmentObjectBinding
import com.example.erabook.util.ARG_TAB_NAME
import com.example.erabook.util.TAB_NAMES
import com.example.erabook.util.showToast
import com.google.android.material.tabs.TabLayout

class ObjectFragment : Fragment() {
    private lateinit var binding: FragmentObjectBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var gestureDetector: GestureDetector

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentObjectBinding.inflate(layoutInflater, container, false)
        viewPager = requireActivity().findViewById(R.id.pager)
        tabLayout = requireActivity().findViewById(R.id.tab_layout)

        gestureDetector =
            GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
                override fun onFling(
                    e1: MotionEvent?,
                    e2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    val sensitivity = 50
                    e1?.let {
                        if (e1.x - e2.x > sensitivity) {
                            val nextTab = tabLayout.selectedTabPosition + 1
                            if (nextTab < tabLayout.tabCount) {
                                viewPager.currentItem = nextTab
                            }
                            return true
                        } else if (e2.x - e1.x > sensitivity) {
                            val previousTab = tabLayout.selectedTabPosition - 1
                            if (previousTab >= 0) {
                                viewPager.currentItem = previousTab
                            }
                            return true
                        }
                    }
                    return false
                }
            })
        binding.root.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { args ->
            val tabName = args.getString(ARG_TAB_NAME)
            val position = TAB_NAMES.indexOfFirst { it.type.name == tabName }
            if (position != -1) {
                binding.sampleText.text = getString(TAB_NAMES[position].tabName)
            }
        }
        binding.googleImage.setOnClickListener {
            requireContext().showToast(R.string.clicked)
        }
    }
}