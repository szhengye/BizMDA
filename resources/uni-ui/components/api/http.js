const AUTH_TOKEN = "X-Auth-Token";

function init(con) {
	//url
	var url = http.baseUrl;
	if (url && con.url && !con.url.match(/^(http|https):\/\/([\w.]+\/?)\S*$/)) {
		if (con.url.charAt(0) !== "/") {
			con.url = "/" + con.url;
		}
		con.url = url.concat(con.url);
	}
	//header
	if (http.header) {
		if (!con.header) {
			con.header = http.header;
		} else {
			Object.keys(http.header).forEach(function(key) {
				con.header[key] = http.header[key]
			});
		}
	}
}

function request(con) {
	init(con);
	console.log("init(con)");
	console.dir(con);
	let config = {
		url: con.url ? con.url : http.baseUrl,
		data: con.data,
		header: con.header,
		method: con.method ? con.method : 'GET',
		dataType: con.dataType ? con.dataType : 'json',
		responseType: con.responseType ? con.responseType : 'text',
		success: con.success ? (res) => {
			if (res.statusCode == 200) {
				if (res.data.code == 200) {
					http.afterResponseFilter(con.success(http.beforeResponseFilter(res)));
				} else {
					if (con.fail) {
						con.fail(res);
					} else {
						uni.showModal({
							content: "(" + res.data.code + ")" + res.data.message,
							showCancel: false
						})
					}
				}
			} else {
				res.data.code = 201;
				res.data.message = "http响应出错:" + res.statusCode.toString();
				if (con.fail) {
					con.fail(res);
				} else {
					uni.showModal({
						content: "(" + res.statusCode + ")HTTP响应出错!",
						showCancel: false
					})

				}
			}
		} : null,
		fail: con.fail ? (res) => {
			con.fail(res);
		} : null,
		complete: con.complete ? (res) => {
			con.complete(res);
		} : null
	}
	return uni.request(http.beforeRequestFilter(config));
}

function get(url, success, con) {
	var conf = {};
	if (typeof success != 'function' && typeof success == 'object' && success != null) {
		conf = success ? success : {};
	} else {
		conf = con ? con : {};
		if (success != undefined && success != null) {
			conf.success = success;
		}
	}

	if (url) {
		conf.url = url
	}

	conf.method = "GET";
	return request(conf);
}

function post(url, data, success, con) {
	var conf = {};
	if (typeof success != 'function' && typeof success == 'object' && success != null) {
		conf = success ? success : {};
	} else {
		conf = con ? con : {};
		if (success != undefined && success != null) {
			conf.success = success;
		}
	}

	if (url) {
		conf.url = url
	}
	if (data) {
		conf.data = data
	}
	if (success != undefined && success != null) {
		conf.success = success;
	}
	conf.method = "POST";
	return request(conf);
}

function uploadFile(con) {
	init(con);

	let config = {
		url: con.url ? con.url : http.baseUrl,
		files: con.files,
		filesType: con.filesType,
		filePath: con.filePath,
		name: con.name,
		header: con.header,
		formData: con.formData,
		success: con.success ? (res) => {
			http.afterResponseFilter(con.success(http.beforeResponseFilter(res)));
		} : null,
		fail: con.fail ? (res) => {
			con.fail(res);
		} : null,
		complete: con.complete ? (res) => {
			con.complete(res);
		} : null
	}
	return uni.uploadFile(http.beforeRequestFilter(config));
}

function downloadFile(con) {
	init(con);

	let config = {
		url: con.url ? con.url : http.baseUrl,
		header: con.header,
		success: con.success ? (res) => {
			http.afterResponseFilter(con.success(http.beforeResponseFilter(res)));
		} : null,
		fail: con.fail ? (res) => {
			con.fail(res);
		} : null,
		complete: con.complete ? (res) => {
			con.complete(res);
		} : null
	}
	return uni.downloadFile(http.beforeRequestFilter(config));
}

function saveToken(token) {
	uni.setStorageSync(AUTH_TOKEN, token);
	http.header[AUTH_TOKEN] = token;
	console.log("saveToken:" + token);
}

let http = {
	// 'setBaseUrl': (url) => {
	// 	if (url.charAt(url.length - 1) === "/") {
	// 		url = url.substr(0, url.length - 1)
	// 	}
	// 	http.baseUrl = url;
	// },
	'baseUrl': "http://localhost:8080/jeecg-boot",
	'header': {},
	'beforeRequestFilter': (config) => {
		return config
	},
	'beforeResponseFilter': (res) => {
		//X-Auth-Token
		if (res.header) {
			var respXAuthToken = res.header[AUTH_TOKEN.toLocaleLowerCase()];
			if (respXAuthToken) {
				uni.setStorageSync(AUTH_TOKEN, respXAuthToken);
				http.header[AUTH_TOKEN] = respXAuthToken;
				console.log("uni.setStorageSync:" + respXAuthToken);
			}
		}
		return res;
	},
	'afterResponseFilter': (successResult) => {},
	'get': get,
	'post': post,
	'request': request,
	'uploadFile': uploadFile,
	'downloadFile': downloadFile,
	'saveToken': saveToken
}

if (uni.getStorageSync(AUTH_TOKEN)) {
    http.header[AUTH_TOKEN] = uni.getStorageSync(AUTH_TOKEN);
	console.log("uni.getStorageSync:"+http.header[AUTH_TOKEN]);
}

export default http
