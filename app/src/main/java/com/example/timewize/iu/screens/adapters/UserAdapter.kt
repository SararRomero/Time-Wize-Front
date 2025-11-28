package com.example.timewize.iu.screens.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.timewize.R
import com.example.timewize.iu.screens.models.User

class UserAdapter(
    private val users: List<User>,
    private val onUserAction: (User, String) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    // Constantes para acciones
    companion object {
        const val ACTION_EDIT = "edit"
        const val ACTION_DELETE = "delete"
        const val ACTION_OPTIONS = "options"
        const val ACTION_VIEW = "view"

        const val STATUS_ACTIVE = "Activo"
        const val STATUS_INACTIVE = "Inactivo"
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Views que existen en tu layout
        val txtName: TextView = itemView.findViewById(R.id.txtUserName)
        val txtEmail: TextView = itemView.findViewById(R.id.txtUserEmail)
        val txtRole: TextView = itemView.findViewById(R.id.txtUserRole)
        val txtStatus: TextView = itemView.findViewById(R.id.txtUserStatus)
        val txtAvatar: TextView = itemView.findViewById(R.id.txtAvatar)
        val btnOptions: ImageButton = itemView.findViewById(R.id.btnOptions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        // Verificar que la posición sea válida
        if (position < 0 || position >= users.size) {
            return
        }

        val user = users[position]

        bindUserData(holder, user)
        setupVisualStyles(holder, user)
        setupClickListeners(holder, user, position)
    }

    override fun getItemCount(): Int = users.size

    /**
     * Configura los datos del usuario en las views
     */
    private fun bindUserData(holder: UserViewHolder, user: User) {
        holder.txtName.text = user.name ?: "Sin nombre"
        holder.txtEmail.text = user.email ?: "Sin email"
        holder.txtRole.text = user.role ?: "Sin rol"
        holder.txtStatus.text = user.status ?: "Sin estado"
        holder.txtAvatar.text = generateUserInitials(user.name ?: "NN")
    }

    /**
     * Configura los estilos visuales
     */
    private fun setupVisualStyles(holder: UserViewHolder, user: User) {
        setupStatusColor(holder, user.status ?: "")
        setupRoleBackground(holder, user.role ?: "")
        setupStatusBackground(holder, user.status ?: "")
    }

    /**
     * Configura el color del texto del estado
     */
    private fun setupStatusColor(holder: UserViewHolder, status: String) {
        val colorRes = when (status) {
            STATUS_ACTIVE -> android.R.color.holo_green_dark
            STATUS_INACTIVE -> android.R.color.holo_red_dark
            else -> android.R.color.darker_gray
        }

        holder.txtStatus.setTextColor(
            ContextCompat.getColor(holder.itemView.context, colorRes)
        )
    }

    /**
     * Configura el fondo del rol del usuario
     */
    private fun setupRoleBackground(holder: UserViewHolder, role: String) {
        try {
            val backgroundRes = when (role.lowercase()) {
                "administrador", "admin" -> R.drawable.bg_role_admin
                "usuario", "user" -> R.drawable.bg_role_user
                else -> R.drawable.bg_role_default
            }
            holder.txtRole.setBackgroundResource(backgroundRes)
        } catch (e: Exception) {
            // Si el drawable no existe, no hacer nada
        }
    }

    /**
     * Configura el fondo del estado del usuario
     */
    private fun setupStatusBackground(holder: UserViewHolder, status: String) {
        try {
            val backgroundRes = when (status) {
                STATUS_ACTIVE -> R.drawable.bg_status_active
                STATUS_INACTIVE -> R.drawable.bg_status_inactive
                else -> R.drawable.bg_status_default
            }
            holder.txtStatus.setBackgroundResource(backgroundRes)
        } catch (e: Exception) {
            // Si el drawable no existe, no hacer nada
        }
    }

    /**
     * Configura los listeners de click CON SEGURIDAD
     */
    private fun setupClickListeners(holder: UserViewHolder, user: User, position: Int) {
        // Listener para el botón de opciones - CON SEGURIDAD
        holder.btnOptions.setOnClickListener {
            try {
                if (position >= 0 && position < users.size) {
                    onUserAction(user, ACTION_OPTIONS)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Listener para el item completo - CON SEGURIDAD
        holder.itemView.setOnClickListener {
            try {
                if (position >= 0 && position < users.size) {
                    onUserAction(user, ACTION_VIEW)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Genera las iniciales del nombre del usuario
     */
    private fun generateUserInitials(fullName: String): String {
        return try {
            fullName.split(" ")
                .mapNotNull { it.firstOrNull()?.toString() }
                .joinToString("")
                .take(2)
                .uppercase()
        } catch (e: Exception) {
            "NN"
        }
    }
}