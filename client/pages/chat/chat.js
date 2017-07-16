var strophe = require('../../utils/strophe.js')
var WebIM = require('../../utils/WebIM.js')
var WebIM = WebIM.default

Page({
    data: {
        show_mask: false,
        myName: '',
        rooms:[]
    },
    onLoad: function (option) {
        this.setData({
            myName: option.myName
        })
    },
    onShow: function () {
        var that = this
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

    into_inform: function () {
        wx.navigateTo({
            url: '../inform/inform'
        })
    },
    add_new: function () {
        wx.navigateTo({
            url: '../add_new/add_new'
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


    tabMain: function () {
        wx.redirectTo({
            url: '../main/main'
        })
    },

    tabSetting: function () {
        wx.redirectTo({
            url: '../settings/settings'
        })
    }

})