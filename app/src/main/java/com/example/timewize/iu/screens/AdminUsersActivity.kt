package com.example.timewize.iu.screens

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.example.timewize.R
import com.example.timewize.iu.screens.models.User
import com.example.timewize.iu.screens.adapters.UserAdapter // IMPORTANTE: Agregar este import

class AdminUsersActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var btnAddUser: com.google.android.material.button.MaterialButton // Cambiado a MaterialButton

    private val userList = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_users)

        initViews()
        setupRecyclerView()
        loadSampleData()
        setupClickListeners()
        setupNavbarListeners()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerViewUsers)
        btnAddUser = findViewById(R.id.btnAddUser) // Asegúrate que el ID coincida con tu XML

        // DEBUG: Verificar que las vistas se encuentran
        if (::recyclerView.isInitialized) {
            println("DEBUG: RecyclerView encontrado")
        } else {
            println("DEBUG: RecyclerView NO encontrado")
        }
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(userList) { user, action ->
            when (action) {
                UserAdapter.ACTION_OPTIONS -> showUserOptions(user) // Cambiado a ACTION_OPTIONS
                UserAdapter.ACTION_VIEW -> showUserDetails(user) // Agregado para el click en el item
            }
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AdminUsersActivity)
            adapter = userAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@AdminUsersActivity,
                    LinearLayoutManager.VERTICAL
                )
            )
            setHasFixedSize(false)
        }

        println("DEBUG: RecyclerView configurado con ${userList.size} usuarios")
    }

    private fun loadSampleData() {
        userList.clear()

        userList.addAll(listOf(
            User("1", "Ana García", "ana@email.com", "Administrador", "Activo"),
            User("2", "Carlos López", "carlos@email.com", "Usuario", "Activo"),
            User("3", "María Rodríguez", "maria@email.com", "Invitado", "Inactivo"),
            User("4", "Juan Pérez", "juan@email.com", "Usuario", "Activo"),
            User("5", "Laura Martínez", "laura@email.com", "Administrador", "Activo")
        ))

        userAdapter.notifyDataSetChanged()

        println("DEBUG: Datos cargados: ${userList.size} usuarios")
        userList.forEach { println("DEBUG: Usuario: ${it.name}") }
    }

    private fun setupClickListeners() {
        btnAddUser.setOnClickListener {
            showUserFormDialog(null)
        }
    }

    // NUEVO MÉTODO: Manejar opciones del usuario
    private fun showUserOptions(user: User) {
        val options = arrayOf("Editar", "Eliminar", "Cambiar Estado")

        AlertDialog.Builder(this)
            .setTitle("Opciones para ${user.name}")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showUserFormDialog(user) // Editar
                    1 -> showDeleteConfirmation(user) // Eliminar
                    2 -> toggleUserStatus(user) // Cambiar Estado
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // NUEVO MÉTODO: Cambiar estado del usuario
    private fun toggleUserStatus(user: User) {
        val newStatus = if (user.status == "Activo") "Inactivo" else "Activo"
        val index = userList.indexOfFirst { it.id == user.id }

        if (index != -1) {
            userList[index] = user.copy(status = newStatus)
            userAdapter.notifyItemChanged(index)
            Toast.makeText(this, "Estado de ${user.name} cambiado a $newStatus", Toast.LENGTH_SHORT).show()
        }
    }

    // NUEVO MÉTODO: Mostrar detalles del usuario (para click en item)
    private fun showUserDetails(user: User) {
        // Aquí puedes mostrar un diálogo con los detalles completos del usuario
        // o navegar a una pantalla de detalles
        Toast.makeText(this, "Ver detalles de ${user.name}", Toast.LENGTH_SHORT).show()
    }

    private fun setupNavbarListeners() {
        // Buscar las vistas del navbar incluido
        val bottomNavAdmin = findViewById<ViewGroup>(R.id.bottomNavigationAdmin)

        val navAdminHome: ImageView? = bottomNavAdmin?.findViewById(R.id.navAdminHome)
        val navCategories: ImageView? = bottomNavAdmin?.findViewById(R.id.navCategories)
        val navReports: ImageView? = bottomNavAdmin?.findViewById(R.id.navReports)
        val navAdminProfile: ImageView? = bottomNavAdmin?.findViewById(R.id.navAdminProfile)
        val navAddAdmin: ImageButton? = bottomNavAdmin?.findViewById(R.id.navAddAdmin)

        navAdminHome?.setOnClickListener {
            // Navegar al Dashboard Admin
            try {
                val intent = Intent(this, AdminDashboardActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this, "Error al navegar al Inicio", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }

        navCategories?.setOnClickListener {
            // Navegar a Categorías
            try {
                val intent = Intent(this, AdminCategoriesActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this, "Pantalla de Categorías no disponible", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }

        navReports?.setOnClickListener {
            // Navegar a Reportes
            try {
                val intent = Intent(this, AdminReportsActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this, "Pantalla de Reportes no disponible", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }

        navAdminProfile?.setOnClickListener {
            Toast.makeText(this, "Perfil Admin - En desarrollo", Toast.LENGTH_SHORT).show()
        }

        // El botón central ya está en la pantalla de usuarios
        navAddAdmin?.setOnClickListener {
            // Ya estamos en usuarios, podemos abrir el diálogo directamente
            showUserFormDialog(null)
        }
    }

    private fun showUserFormDialog(user: User?) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_user_form)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(true)

        val etName = dialog.findViewById<TextInputEditText>(R.id.etUserName)
        val etEmail = dialog.findViewById<TextInputEditText>(R.id.etUserEmail)
        val etPassword = dialog.findViewById<TextInputEditText>(R.id.etUserPassword)
        val radioGroupRole = dialog.findViewById<RadioGroup>(R.id.radioGroupRole)
        val btnSave = dialog.findViewById<Button>(R.id.btnSave)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)

        // Configurar el título del diálogo
        dialog.setTitle(if (user == null) "Agregar Usuario" else "Editar Usuario")

        // Si es edición, llenar los campos
        user?.let {
            etName.setText(it.name)
            etEmail.setText(it.email)
            when (it.role) {
                "Administrador" -> radioGroupRole.check(R.id.radioAdmin)
                "Invitado" -> radioGroupRole.check(R.id.radioGuest)
                else -> radioGroupRole.check(R.id.radioUser)
            }
            // Ocultar campo de contraseña en edición
            etPassword.visibility = View.GONE
        } ?: run {
            // Mostrar campo de contraseña para nuevo usuario
            etPassword.visibility = View.VISIBLE
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            if (validateForm(name, email, if (user == null) password else "dummy")) {
                if (user == null) {
                    // Crear nuevo usuario
                    val newUser = User(
                        id = (System.currentTimeMillis()).toString(),
                        name = name,
                        email = email,
                        role = getSelectedRole(radioGroupRole),
                        status = "Activo"
                    )
                    userList.add(0, newUser)
                    userAdapter.notifyItemInserted(0)
                    recyclerView.smoothScrollToPosition(0)
                    Toast.makeText(this, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show()
                } else {
                    // Actualizar usuario existente
                    val index = userList.indexOfFirst { it.id == user.id }
                    if (index != -1) {
                        userList[index] = user.copy(
                            name = name,
                            email = email,
                            role = getSelectedRole(radioGroupRole)
                        )
                        userAdapter.notifyItemChanged(index)
                        Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.dismiss()
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            name.isEmpty() -> {
                Toast.makeText(this, "Ingrese el nombre completo", Toast.LENGTH_SHORT).show()
                false
            }
            email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, "Ingrese un email válido", Toast.LENGTH_SHORT).show()
                false
            }
            password.isEmpty() && password != "dummy" -> {
                Toast.makeText(this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show()
                false
            }
            password.length < 6 && password != "dummy" -> {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun getSelectedRole(radioGroup: RadioGroup): String {
        return when (radioGroup.checkedRadioButtonId) {
            R.id.radioAdmin -> "Administrador"
            R.id.radioGuest -> "Invitado"
            else -> "Usuario"
        }
    }

    private fun showDeleteConfirmation(user: User) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Usuario")
            .setMessage("¿Está seguro de eliminar a ${user.name}?")
            .setPositiveButton("Eliminar") { _, _ ->
                val index = userList.indexOfFirst { it.id == user.id }
                if (index != -1) {
                    userList.removeAt(index)
                    userAdapter.notifyItemRemoved(index)
                    Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        println("DEBUG: onResume - UserList size: ${userList.size}")
        println("DEBUG: onResume - Adapter item count: ${userAdapter.itemCount}")
    }
}