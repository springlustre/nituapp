var strophe = require('../../utils/strophe.js')
var WebIM = require('../../utils/WebIM.js')
var WebIM = WebIM.default

Page({
    data: {
        search_btn: true,
        search_friend: false,
        show_mask: false,
        myName: '',
        member: [],
        rooms:[]
    },
    onLoad: function (option) {
        this.setData({
            myName: option.myName
        })
        //console.log("wjy")
    },
    onShow: function () {
        var that = this
        // //console.log(WebIM.conn)
        var rosters = {
            success: function (roster) {
                var member = []
                for (var i = 0; i < roster.length; i++) {
                    if (roster[i].subscription == "both") {
                        member.push(roster[i])
                    }
                }
                that.setData({
                    member: member
                })
                wx.setStorage({
                    key: 'member',
                    data: that.data.member
                })
            }
        }

        //WebIM.conn.setPresence()
        WebIM.conn.getRoster(rosters)

        // 列出所有群组
        this.listGroups()
    },

    listGroups : function () {
      var that = this
        var option = {
            success: function (rooms) {
                console.log("------======="+JSON.stringify(rooms));
                var r = []
                for(var i = 0;i<rooms.length;i++){
                  var t = {}
                  t.name = rooms[i].name
                  t.roomId = rooms[i].roomId
                  r.push(t)
                    r.push(t)
                }
                that.setData({
                  rooms: r
                })
            },
            error: function () {
                console.log('List groups error');
            }
        };
        WebIM.conn.listRooms(option);
    },

    moveFriend: function (message) {
        var that = this
        var rosters = {
            success: function (roster) {
                var member = []
                for (var i = 0; i < roster.length; i++) {
                    if (roster[i].subscription == "both") {
                        member.push(roster[i])
                    }
                }
                that.setData({
                    member: member
                })
            }
        }
        if (message.type == 'unsubscribe' || message.type == 'unsubscribed') {
            WebIM.conn.removeRoster({
                to: message.from,
                success: function () {
                    WebIM.conn.unsubscribed({
                        to: message.from
                    });
                    WebIM.conn.getRoster(rosters)
                }
            })
        }
    },
    handleFriendMsg: function (message) {
        var that = this
        //console.log(message)
        wx.showModal({
            title: '添加好友请求',
            content: message.from + '请求加为好友',
            success: function (res) {
                if (res.confirm == true) {
                    //console.log('vvvvvvvvvvvvv')
                    WebIM.conn.subscribed({
                        to: message.from,
                        message: "[resp:true]"
                    })
                    WebIM.conn.subscribe({
                        to: message.from,
                        message: "[resp:true]"
                    })
                } else {
                    WebIM.conn.unsubscribed({
                        to: message.from,
                        message: "rejectAddFriend"
                    })
                    //console.log('delete_friend')
                }
            },
            fail: function (error) {
                //console.log(error)
            }
        })

    },
    delete_friend: function (event) {
        var delName = event.target.dataset.username
        wx.showModal({
            title: '确认删除好友' + delName,
            cancelText: '取消',
            confirmText: '删除',
            success: function (res) {
                if (res.confirm == true) {
                    WebIM.conn.removeRoster({
                        to: delName,
                        success: function () {
                            WebIM.conn.unsubscribed({
                                to: delName
                            });
                            //console.log(delName)
                        },
                        error: function (error) {
                            //console.log(error)
                        }
                    })
                }

            },
            fail: function (error) {
                //console.log(error)
            }
        })
    },
    openSearch: function () {
        this.setData({
            search_btn: false,
            search_friend: true,
            show_mask: true
        })
    },
    cancel: function () {
        this.setData({
            search_btn: true,
            search_friend: false,
            show_mask: false
        })
    },
    add_new: function () {
        wx.navigateTo({
            url: '../add_new/add_new'
        })
    },
    tab_chat: function () {
        wx.redirectTo({
            url: '../chat/chat'
        })
    },
    close_mask: function () {
        this.setData({
            search_btn: true,
            search_friend: false,
            show_mask: false
        })
    },
    tab_setting: function () {
        wx.redirectTo({
          url: '../settings/settings'
        })
    },
    into_inform: function () {
        wx.navigateTo({
            url: '../inform/inform'
        })
    },
    into_groups: function () {
        wx.navigateTo({
            url: '../groups/groups'
        })
    },
    into_room: function (event) {
        var that = this
        //console.log(event)
        var nameList = {
            myName: that.data.myName,
            your: event.target.dataset.username
        }
        wx.navigateTo({
            url: '../chatroom/chatroom?username=' + JSON.stringify(nameList)
        })
    },
    into_group: function (event) {
        var that = this
        console.log(event)
        var nameList = {
            myName: that.data.myName,
            your: event.currentTarget.dataset.username,
            groupId:event.currentTarget.dataset.groupid
        }

        wx.navigateTo({
            url: '../chatroom/chatroom?username=' + JSON.stringify(nameList)
        })
    },
    into_info: function (event) {
        wx.navigateTo({
            url: '../friend_info/friend_info?yourname=' + event.target.dataset.username
        })
    },

    tabRooms: function () {
        wx.redirectTo({
            url: '../chat/chat'
        })
    },

    tabSetting: function () {
        wx.redirectTo({
            url: '../settings/settings'
        })
    }

})