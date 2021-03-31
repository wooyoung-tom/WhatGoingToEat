package tom.dev.whatgoingtoeat.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.ActivityMainBinding
import tom.dev.whatgoingtoeat.utils.hide
import tom.dev.whatgoingtoeat.utils.show

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    // Toolbar configuration Variable
    private lateinit var toolbarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        // Init navHost and navController
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHost.navController

        setSupportActionBar(binding.toolbarMain)

        // Top Level Fragment IDs
        toolbarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment)
        )

        NavigationUI.setupWithNavController(binding.toolbarMain, navController, toolbarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.signInFragment, R.id.signUpFragment -> binding.toolbarMain.hide()
                R.id.homeFragment, R.id.restaurantFragment -> setToolbar()
            }
        }
    }

    private fun setToolbar() {
        binding.toolbarMain.title = "런치마켓"
        binding.toolbarMain.show()
    }
}