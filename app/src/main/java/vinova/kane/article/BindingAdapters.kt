/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package vinova.kane.article

//@BindingAdapter("imageUrl")
//fun bindImage(imgView: ImageView, imgUrl: String?){
//    Log.d("BindingAdapter", "Binding Image")
//    imgUrl?.let {
//        val url = Constant.IMAGE_URL + imgUrl
//        Glide.with(imgView.context)
//            .load(url)
//            .apply(RequestOptions()
//                .placeholder(R.drawable.loading_animation)
//                .error(R.drawable.ic_broken_image))
//            .into(imgView)
//        Log.d("BindingAdapter", "Image URL: $url")
//    }
//}
//
//@BindingAdapter("listData")
//fun bindRecyclerView(recyclerView: RecyclerView, data: List<Doc>?){
//    val adapter = recyclerView.adapter as ArticleAdapter
////    adapter.submitList(data)
//    Log.d("BindingAdapter", "Doc's size: ${adapter.itemCount}")
//}