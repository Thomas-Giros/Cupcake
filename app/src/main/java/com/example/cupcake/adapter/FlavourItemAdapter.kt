package com.example.cupcake.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.cupcake.R
import com.example.cupcake.model.OrderViewModel
import kotlinx.coroutines.currentCoroutineContext

class FlavourItemAdapter(private val dataset: List<String>,
                         private val sharedViewModel: OrderViewModel
) : RecyclerView.Adapter<FlavourItemAdapter.ItemViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just an Affirmation object.
    class ItemViewHolder( view: View) : RecyclerView.ViewHolder(view) {
        val addButton: Button = view.findViewById(R.id.buttonAdd)
        val subButton: Button = view.findViewById(R.id.buttonSub)
        val flavourTextView: TextView = view.findViewById(R.id.flavour_textView)
        val numCupCakesView: TextView = view.findViewById(R.id.numberCupCake)
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_flavour_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.flavourTextView.text = item
        holder.numCupCakesView.text = sharedViewModel.orderedQuantityPerFlavor.value?.get(item).toString()

        holder.addButton.setOnClickListener { sharedViewModel.addFlavor(item)
            holder.numCupCakesView.text = sharedViewModel.orderedQuantityPerFlavor.value?.get(item).toString()
        }
        holder.subButton.setOnClickListener { sharedViewModel.removeFlavor(item)
            holder.numCupCakesView.text = sharedViewModel.orderedQuantityPerFlavor.value?.get(item).toString()
        }

    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = dataset.size
}
