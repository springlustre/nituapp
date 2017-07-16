var strophe = require('../../utils/strophe.js')
var WebIM = require('../../utils/WebIM.js')
var WebIM = WebIM.default

Page({
	data: {
		username:''
	},
	onLoad: function() {
		var myUsername = wx.getStorageSync('myUsername')
		this.setData({
			username: myUsername
		})
		console.log("+++++++++++++++ laod")
	},
	person: function() {
		var myUsername = wx.getStorageSync('myUsername')
		wx.navigateTo({
			url: '../friend_info/friend_info?yourname=' + myUsername
		})
	},

    tabRooms: function () {
        wx.redirectTo({
            url: '../chat/chat'
        })
    },
    tabMain: function () {
		console.log("----to main")
        wx.redirectTo({
            url: '../main/main'
        })
    },

	logout: function() {
		wx.showModal({
			title: '是否退出登录',
			success: function(res) {
			    if (res.confirm) {
		   	        WebIM.conn.close()
		   	        //wx.closeSocket()
		   	        wx.redirectTo({
		   	        	url: '../login/login'
		   	        })
				}
			}
		})
	}
})