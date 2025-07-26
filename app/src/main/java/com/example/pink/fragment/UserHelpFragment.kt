package com.example.pink.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.pink.R

class UserHelpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = inflater.inflate(R.layout.fragment_user_help, container, false)

        val contactBtn: Button = view.findViewById(R.id.contactSupportBtn)
        contactBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:soporte@pinkapp.com".toUri()
                putExtra(Intent.EXTRA_SUBJECT, "Consulta desde la app")
            }
            startActivity(Intent.createChooser(intent, "Enviar correo"))
        }

        return view
    }
}