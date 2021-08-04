package com.nextgenmw.simplechatapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nextgenmw.simplechatapp.App
import com.nextgenmw.simplechatapp.adapters.MessageAdapter
import com.nextgenmw.simplechatapp.databinding.FragmentSecondBinding
import com.nextgenmw.simplechatapp.models.Message
import com.nextgenmw.simplechatapp.network.ChatService
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ChatFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupPusher()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.messageList.layoutManager = LinearLayoutManager(
            requireContext()
        )
        val adapter = MessageAdapter(requireContext())
        binding.messageList.adapter = adapter

        binding.btnSend.setOnClickListener {
            if(binding.txtMessage.text.isNotEmpty()) {
                val message = Message(
                    App.user,
                    binding.txtMessage.text.toString(),
                    Calendar.getInstance().timeInMillis
                )

                val call = ChatService.create().postMessage(message)

                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {

                        if (!response.isSuccessful) {
                            Log.e("Chat Fragment", response.code().toString());
                            Toast.makeText(
                                requireContext()
                                ,"Response was not successful",
                                Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {

                        Log.e("Chat Fragment", t.toString());
                        Toast.makeText(
                            requireContext(),
                            "Error when calling the service",
                            Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(
                    requireContext()
                    ,"Message should not be empty",
                    Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setupPusher() {
        val options = PusherOptions()
        options.setCluster("PUSHER_APP_CLUSTER")

        val pusher = Pusher("f97d4b7dc89aaa6c769f", options)
        val channel = pusher.subscribe("chat")

        channel.bind("new_message") { _, _, data ->
            val jsonObject = JSONObject(data)

            val message = Message(
                jsonObject["user"].toString(),
                jsonObject["message"].toString(),
                jsonObject["time"].toString().toLong()
            )

            adapter.addMessage(message)
            // scroll the RecyclerView to the last added element
            binding.messageList.scrollToPosition(adapter.itemCount - 1);

        }

        pusher.connect()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}