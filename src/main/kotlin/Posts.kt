import java.time.LocalDate

data class Posts<T>(
    var id: Int? = null,
    val ownerId: Int,
    val fromId: Int? = null,
    val createdBy: Int? = null,
    val date: LocalDate = LocalDate.now(),
    var text: String? = null,
    val replyOwnerId: Int? = null,
    val replyPostId: Int? = null,
    val friendsOnly: Boolean = false,
    val comments: Comments = Comments(),
    val likes: Likes = Likes(),
    val reposts: Reposts = Reposts(),
    val views: Views? = null,
    val postType: PostType = PostType.REPLY,
    val canPin: Boolean = false,
    val canDelete: Boolean = true,
    val canEdit: Boolean = true,
    val isPinned: Boolean = false,
    val markedAsAds: Boolean = false,
    val isFavorite: Boolean = false,
    val postponedId: Int? = null,
    val attachments: Array<Attachments> = emptyArray()
) {
    enum class PostType {
        POST, COPY, REPLY, POSTPONE, SUGGEST
    }

    data class Comments(
        val count: Int = 0,
        val canPost: Boolean = true,
        val groupsCanPost: Boolean? = null,
        val canClose: Boolean = true,
        val canOpen: Boolean = true
    )

    class Likes(
        val count: Int = 0,
        val userLikes: Boolean = true,
        val canLike: Boolean = true,
        val canPublish: Boolean = true
    )

    class Reposts(
        val count: Int = 0,
        val userReposted: Boolean = false
    )

    data class Views(
        val count: Int? = null
    )
}

interface Attachments {
    val type: String
}
data class VideoAttachments (
    override val type: String = "video",
    val video: Video
): Attachments
data class Video(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val description: String? = null,
    val duration: Int,
    val image: List<Image> = emptyList()
) {
    data class Image(
        val url: String,
        val width: Int,
        val height: Int
    )
}

data class AudioAttachments(
    override val type: String = "audio",
    val audio: Audio
): Attachments
data class Audio(
    val id: Int,
    val ownerId: Int,
    val artist: String,
    val title: String,
    val duration: Int,
    val url: String? = null
)

data class DocumentAttachment(
    override val type: String = "Doc",
    val document: Document
): Attachments
data class Document(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val size: Int,
    val url: String? = null
)

data class LInkAttachment(
    override val type: String = "Link",
    val link: Link
) : Attachments
data class Link(
    val url: String,
    val title: String,
    val caption: String? = null
)

data class AlbumAttachments (
    override val type: String = "albumAttachments",
    val album: Album
): Attachments
data class Album (
    val id: Int,
    val thumb: Photo,
    val ownerId: Int,
    val title: String,
    val description: String? = null,
    val created: Long,
    val updated: Long,
    val size: Int
) {
    data class Photo(
        val id: Int,
        val albumId: Int,
        val ownerId: Int,
        val userId: Int? = null,
        val text: String? = null,
        val date: Long? = null,
        val sizes: Array<Size> = emptyArray(),
        val width: Int? = null,
        val height: Int? = null
    ) {
        data class Size(
            val type: String,
            val url: String,
            val width: Int,
            val height: Int
        )
    }
}
class PostNotFoundException(s: String) : Throwable() {

}
object WallService {
    private var posts = emptyArray<Posts<Any?>>()
    private var uniqId: Int = 0
    var comments = emptyArray<Comment>()
    data class Comment(
        val id: Int,
        val postId: Int,
        val fromId: Int?,
        val text: String?,
        val date: Long?
    )
    fun createComment(postId: Int, comment: Comment): Comment {
        val post = posts.find { it.id == postId }
        if (post != null) {
            val newComment = comment.copy (
                id = comments.size + 1,
                postId = postId
            )
            comments += newComment
            return newComment
        } else {
            throw PostNotFoundException ("Пост с таким ID $postId не найден.")
        }
    }

    fun add(post: Posts<Any?>): Posts<Any?> {
        uniqId++
        val newPost = post.copy()
        newPost.id = uniqId
        newPost.text = "Запись #$uniqId"
        posts += newPost
        return newPost
    }

    fun update(post: Posts<Any?>): Boolean {
        for (index in posts.indices) {
            if (posts[index].id == post.id) {
                posts[index] = post.copy()
                return true
            }
        }
        return false
    }

    fun clear() {
        posts = emptyArray()
        uniqId = 0
    }
}