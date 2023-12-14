class ListNode(var `val`: Int, var next: ListNode?)


class Solution {
    fun reverseList(head: ListNode?): ListNode? {
        if (head == null || head.next == null) {
            return head
        }

        val newHead = reverseList(head.next)
        head.next!!.next = head
        head.next = null

        return newHead
    }
}

fun main() {
    val s = Solution()
    val head = ListNode(1, ListNode(2, ListNode(3, ListNode(4, ListNode(5, null)))))
    var node = s.reverseList(head)
    while (node != null) {
        println(node.`val`)
        node = node.next
    }
}
