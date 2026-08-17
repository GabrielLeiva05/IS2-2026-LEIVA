import './App.css'
import { TwitterFollowCard } from './TwitterFollowCard'

export function App() {
const formatUserName = (userName) => `@${userName}`

  return (
    <section className='App'>
      <TwitterFollowCard 
        formatUserName={formatUserName}
        isFollowing 
        userName="midudev" 
        name="Miguel Ángel Durán"/>

      <TwitterFollowCard 
        formatUserName={formatUserName} 
        isFollowing 
        userName="pheralb" 
        name="Pablo Hernandez"/>

      <TwitterFollowCard 
        formatUserName={formatUserName} 
        userName="willyrexYT" 
        name="Guillermo Díaz"/>

      <TwitterFollowCard 
        formatUserName={formatUserName} 
        userName="Vegetta777" 
        name="Samuel De Luque"/>
    </section>
  )
}