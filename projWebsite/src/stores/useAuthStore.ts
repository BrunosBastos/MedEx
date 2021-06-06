import createStore from "zustand";
import persist from 'src/stores/utils/persist.js';


const useAuthStore = createStore(
    persist(
        {
            key:"auth",
        },
        (set) => ({
            token: String,
            isSuperUser: Boolean,
            login: (token: string, isSuperUser: boolean) => {
                set((state) => ({
                    token: token,
                    isSuperUser: isSuperUser
                }))
            },
            exit: () => {
                set((state) => ({
                    token: null,
                    isSuperUser: null,
                }))
            }
        })
    ),
)

export default useAuthStore;