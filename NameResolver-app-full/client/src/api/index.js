import axios from 'axios'

const api = axios.create({
    baseURL: 'http://irt-beagle.cs.columbia.edu/api',
})

export const insertDevice = payload => api.post(`/device`, payload)
export const getAllDevices = () => api.get(`/devices`)
export const updateDeviceById = (id, payload) => api.put(`/device/${id}`, payload)
export const updateDevicePixelById = (id, payload) => api.put(`/device/pixel/${id}`, payload)
export const deleteDeviceById = id => api.delete(`/device/${id}`)
export const getDeviceById = id => api.get(`/device/${id}`)
export const getRoomById = id => api.get(`/room/${id}`)
export const getAllTypes = () => api.get(`/deviceTypes`)
export const getTypeById = id => api.get(`/deviceType/${id}`)
export const getAllBuildings = () => api.get(`/buildings`)
export const getFloorsByBuilding = payload => api.post(`/floorsByBuilding`, payload)
export const getRoomsByFloor = payload => api.post(`/roomsByFloor`, payload)
export const getDevicesByRoom = payload => api.post(`/devicesByRoom`, payload)
export const getDevicesByFloor = payload => api.post(`/devicesByFloor`, payload)
export const getFloorById = id => api.get(`/floor/${id}`)
export const getBuildingById = id => api.get(`/building/${id}`)
export const updateFloorPlanById = (id,payload) => api.put(`/floor/refpoints/${id}`, payload)

const apis = {
    insertDevice,
    getAllDevices,
    updateDeviceById,
    updateDevicePixelById,
    deleteDeviceById,
    getDeviceById,
    getRoomById,
    getAllTypes,
    getAllBuildings,
    getFloorsByBuilding,
    getRoomsByFloor,
    getDevicesByRoom,
    getDevicesByFloor,
    getTypeById,
    getFloorById,
    getBuildingById,
    updateFloorPlanById,
}

export default apis
